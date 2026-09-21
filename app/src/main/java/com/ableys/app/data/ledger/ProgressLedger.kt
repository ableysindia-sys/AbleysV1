package com.ableys.app.data.ledger

import com.ableys.app.data.local.ChildProfileDao
import com.ableys.app.data.local.SkillDao
import com.ableys.app.data.model.SkillArea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.UUID

/**
 * Appends to the ledger and keeps the fast-read projection in step with it.
 *
 * The ledger is the truth. The columns the UI reads -- a child's XP, level, minutes, active days
 * -- are a projection of it, maintained on write so a screen never has to sum a table, and
 * rebuildable from the ledger at any time. That last part is what makes this event sourcing
 * rather than an audit log nobody trusts: if the projection is ever wrong, it can be thrown away
 * and recomputed, and a test proves it.
 *
 * It also settles a contradiction worth naming. Purging synced rows to keep the database small
 * would destroy the only record the totals are derived from. The rows stay. They are about a
 * hundred bytes each, and a family doing five things a day writes under two hundred kilobytes a
 * year, which is less than one of the photos this app already stores happily.
 */
class ProgressLedger(
    private val events: ProgressEventDao,
    private val children: ChildProfileDao,
    private val skills: SkillDao,
    /**
     * Optional so the ledger can be constructed without the achievement side in tests that are
     * only about events. When present, every append re-checks the badges.
     */
    private val achievements: AchievementEvaluator? = null
) {
    // Sequence allocation has to be serialised or two concurrent completions collide on the same
    // number, and ordering is the one thing this design cannot get wrong.
    private val sequenceLock = Mutex()

    suspend fun append(
        childId: String,
        eventType: String,
        subjectId: String,
        xpEarned: Int = 0,
        minutesMoved: Int = 0,
        eventId: String = UUID.randomUUID().toString(),
        occurredAt: Long = System.currentTimeMillis()
    ): ProgressEvent = withContext(Dispatchers.IO) {
        val event = sequenceLock.withLock {
            val next = events.highestSequence() + 1
            ProgressEvent(
                id = eventId,
                childId = childId,
                sequence = next,
                eventType = eventType,
                subjectId = subjectId,
                xpEarned = xpEarned,
                minutesMoved = minutesMoved,
                occurredAt = occurredAt,
                localDay = ProgressEvent.localDayOf(occurredAt)
            ).also { events.append(it) }
        }
        project(childId)
        event
    }

    /**
     * Recomputes the projection for one child from the ledger.
     *
     * Called after every append, and callable on its own. If the projection and the ledger ever
     * disagree, the ledger wins: this is the function that makes that true rather than aspirational.
     */
    suspend fun project(childId: String) = withContext(Dispatchers.IO) {
        val profile = children.getProfile(childId) ?: return@withContext
        val xp = events.totalXp(childId)
        val minutes = events.totalMinutes(childId)
        val days = events.activeDays(childId)
        val movementDays = events.movementDays(childId)
        val today = ProgressEvent.localDayOf(System.currentTimeMillis())

        children.insertOrUpdateProfile(
            profile.copy(
                totalXp = xp,
                level = levelFor(xp),
                minutesMoved = minutes,
                activeDays = days,
                currentStreak = StreakCalculator.streakOn(movementDays, today),
                lastActiveDay = movementDays.lastOrNull()
            )
        )

        // Badges are a projection of the same ledger. Failing to award one must not fail the
        // append that earned it, so this cannot throw into the caller.
        runCatching { achievements?.evaluate(childId) }

        // Per-area skill progress is a projection too, so a rebuild fixes it as well.
        SkillArea.entries.forEach { area ->
            val existing = skills.getSkillProgress(childId, area.id) ?: return@forEach
            val areaXp = events.xpFor(childId, ProgressEvent.SKILL_GAME_COMPLETE, area.id)
            val games = events.countFor(childId, ProgressEvent.SKILL_GAME_COMPLETE, area.id)
            skills.insertAll(
                listOf(
                    existing.copy(
                        xpEarned = areaXp,
                        gamesCompleted = games,
                        currentLevel = levelFor(areaXp)
                    )
                )
            )
        }
    }

    /** Throws the projection away and rebuilds it. The ledger is unaffected. */
    suspend fun rebuild(childId: String) = project(childId)

    private fun levelFor(xp: Int): Int = xp / 200 + 1
}
