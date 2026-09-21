package com.ableys.app.data.ledger

import com.ableys.app.data.local.AchievementDao
import com.ableys.app.data.local.MemoryDao
import com.ableys.app.data.model.Achievement
import com.ableys.app.data.model.MemoryItem
import com.ableys.app.data.model.MemorySource
import com.ableys.app.data.model.MoveFormat
import com.ableys.app.data.model.SkillArea
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Decides which badges a child has earned, from the ledger and nothing else.
 *
 * Every badge in the catalogue rendered permanently locked before this existed: the catalogue
 * described eight milestones and no code ever moved one. Progress is also shown, not just the
 * unlock, because "3 of 7 days" is the part that makes a locked badge encouraging rather than
 * a list of things a family has failed to do.
 *
 * Counted rather than incremented, for the same reason the streak is: a badge awarded by a
 * counter that drifted is unrecoverable, and a badge revoked because a counter was rebuilt is
 * worse. Deriving from the ledger makes both impossible.
 */
class AchievementEvaluator(
    private val events: ProgressEventDao,
    private val achievements: AchievementDao,
    private val memories: MemoryDao,
    /** Maps a completed activity id to its format. Injected so this stays testable without content. */
    private val formatOf: (String) -> MoveFormat?,
    /** Maps a completed activity id to the skill area it targets. */
    private val areaOf: (String) -> SkillArea?
) {

    /** Everything the eight thresholds are measured against. */
    data class Counters(
        val streak: Int = 0,
        val movementDays: Int = 0,
        val skillGames: Int = 0,
        val memoryDays: Int = 0,
        val formatsTried: Int = 0,
        val independenceDays: Int = 0,
        val minutesMoved: Int = 0,
        val activeDays: Int = 0
    )

    suspend fun evaluate(childId: String): List<Achievement> {
        val counters = gather(childId)
        val existing = achievements.forChild(childId)
        if (existing.isEmpty()) return emptyList()

        val today = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())
        val newlyUnlocked = mutableListOf<Achievement>()

        val updated = existing.map { badge ->
            val progress = progressFor(badge.code, counters).coerceAtMost(badge.maxProgress)
            val earned = progress >= badge.maxProgress
            when {
                // Already unlocked stays unlocked. A badge that could be taken away by a
                // recount is not a reward, and a child who watched it appear would see it go.
                badge.isUnlocked -> badge.copy(progress = badge.maxProgress)
                earned -> badge.copy(progress = progress, isUnlocked = true, unlockedDate = today)
                    .also { newlyUnlocked += it }
                else -> badge.copy(progress = progress)
            }
        }
        achievements.insertAll(updated)

        // The spec has an earned badge land on the My Story timeline. Written after the badge,
        // so a failure here cannot cost the child the badge itself.
        newlyUnlocked.forEach { badge ->
            memories.insertMemory(
                MemoryItem(
                    childId = childId,
                    title = badge.title,
                    caption = badge.description,
                    dateString = today,
                    source = MemorySource.ABLEY_AUTO,
                    badgeTag = "Milestone",
                    highlightColorHex = 0xFFEE4A41,
                    iconEmoji = "🏅"
                )
            )
        }
        return newlyUnlocked
    }

    private suspend fun gather(childId: String): Counters {
        val movementDays = events.movementDays(childId)
        val activityRows = events.subjectDays(childId, ProgressEvent.ACTIVITY_COMPLETE)

        return Counters(
            streak = StreakCalculator.streakOn(
                movementDays,
                ProgressEvent.localDayOf(System.currentTimeMillis())
            ),
            movementDays = movementDays.size,
            skillGames = events.countOfType(childId, ProgressEvent.SKILL_GAME_COMPLETE),
            memoryDays = events.distinctDaysOfType(childId, ProgressEvent.MEMORY_CAPTURED),
            formatsTried = activityRows.mapNotNull { formatOf(it.subjectId) }.distinct().size,
            independenceDays = (
                activityRows
                    .filter { areaOf(it.subjectId) == SkillArea.EVERYDAY_INDEPENDENCE }
                    .map { it.localDay } +
                    events.daysOfSubject(
                        childId,
                        ProgressEvent.SKILL_GAME_COMPLETE,
                        SkillArea.EVERYDAY_INDEPENDENCE.id
                    )
                ).distinct().size,
            minutesMoved = events.totalMinutes(childId),
            activeDays = events.activeDays(childId)
        )
    }

    companion object {
        /**
         * What each badge counts. Pure, so the thresholds can be tested without a database.
         *
         * An unknown code returns 0 rather than throwing: a badge added to the catalogue without
         * a rule here should sit at zero, not crash the app on the next completed activity.
         */
        fun progressFor(code: String, c: Counters): Int = when (code) {
            "7_day_explorer" -> c.streak
            "30_day_movement" -> c.movementDays
            "100_skills" -> c.skillGames
            "365_moments" -> c.memoryDays
            "little_adventurer" -> c.formatsTried
            "independent_me" -> c.independenceDays
            "movement_500" -> c.minutesMoved
            "one_year_growing" -> c.activeDays
            else -> 0
        }
    }
}
