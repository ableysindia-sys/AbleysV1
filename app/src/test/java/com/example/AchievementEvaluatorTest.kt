package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.content.AchievementCatalogue
import com.example.data.ledger.AchievementEvaluator
import com.example.data.ledger.ProgressEvent
import com.example.data.ledger.ProgressLedger
import com.example.data.local.AppDatabase
import com.example.data.model.ChildProfile
import com.example.data.model.MoveFormat
import com.example.data.model.SkillArea
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AchievementEvaluatorTest {

    private lateinit var db: AppDatabase
    private lateinit var ledger: ProgressLedger
    private val child = "child-1"

    // A tiny stand-in catalogue of activities, so the test does not depend on the real corpus.
    private val formats = mapOf(
        "a_quick" to MoveFormat.QUICK,
        "a_daily" to MoveFormat.DAILY,
        "a_anywhere" to MoveFormat.ANYWHERE,
        "a_focused" to MoveFormat.FOCUSED,
        "a_week" to MoveFormat.ONE_WEEK,
        "a_month" to MoveFormat.ONE_MONTH
    )
    private val areas = mapOf("a_dressing" to SkillArea.EVERYDAY_INDEPENDENCE)

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val evaluator = AchievementEvaluator(
            events = db.progressEventDao(),
            achievements = db.achievementDao(),
            memories = db.memoryDao(),
            formatOf = { formats[it] },
            areaOf = { areas[it] }
        )
        ledger = ProgressLedger(db.progressEventDao(), db.childProfileDao(), db.skillDao(), evaluator)
        runBlocking {
            db.childProfileDao().insertOrUpdateProfile(ChildProfile(id = child, name = "Test"))
            db.achievementDao().insertAll(AchievementCatalogue.locked.map { it.copy(childId = child) })
        }
    }

    @After
    fun tearDown() = db.close()

    private suspend fun move(day: String, id: String = "a_quick", minutes: Int = 10) {
        ledger.append(
            childId = child,
            eventType = ProgressEvent.ACTIVITY_COMPLETE,
            subjectId = id,
            xpEarned = 10,
            minutesMoved = minutes,
            occurredAt = dayMillis(day)
        )
    }

    private fun dayMillis(day: String): Long {
        val parts = day.split("-").map { it.toInt() }
        return java.util.GregorianCalendar(parts[0], parts[1] - 1, parts[2], 12, 0).timeInMillis
    }

    private suspend fun badge(code: String) = db.achievementDao().forChild(child).first { it.code == code }

    @Test
    fun `every badge starts locked at zero`() = runBlocking {
        val all = db.achievementDao().forChild(child)
        assertEquals(8, all.size)
        assertTrue("no badge may start earned", all.none { it.isUnlocked })
    }

    @Test
    fun `progress is shown long before a badge unlocks`() = runBlocking {
        move("2026-09-19"); move("2026-09-20"); move("2026-09-21")

        val explorer = badge("7_day_explorer")
        assertEquals("a locked badge still has to show the distance", 3, explorer.progress)
        assertFalse(explorer.isUnlocked)
    }

    @Test
    fun `minutes accumulate toward Movement 500 and unlock it exactly once`() = runBlocking {
        repeat(8) { i -> move("2026-09-${19 + (i % 3)}", minutes = 60) }
        assertFalse("480 minutes is short of 500", badge("movement_500").isUnlocked)

        move("2026-09-21", minutes = 60)
        val earned = badge("movement_500")
        assertTrue(earned.isUnlocked)
        assertEquals("progress is capped at the threshold", 500, earned.progress)

        // Cascades exactly one milestone memory, and does not cascade again on the next event.
        val afterFirst = db.memoryDao().memoriesFor(child).count { it.badgeTag == "Milestone" }
        move("2026-09-22", minutes = 60)
        assertEquals(
            "a badge must not re-award on every later event",
            afterFirst,
            db.memoryDao().memoriesFor(child).count { it.badgeTag == "Milestone" }
        )
    }

    @Test
    fun `Little Adventurer needs all six formats, not six sessions`() = runBlocking {
        repeat(6) { move("2026-09-21", id = "a_quick") }
        assertEquals("six of the same format is one format", 1, badge("little_adventurer").progress)

        formats.keys.forEach { move("2026-09-21", id = it) }
        assertTrue(badge("little_adventurer").isUnlocked)
    }

    @Test
    fun `Independent Me counts days of everyday independence, not every activity`() = runBlocking {
        listOf("2026-09-17", "2026-09-18", "2026-09-19", "2026-09-20", "2026-09-21").forEach {
            move(it, id = "a_quick")          // movement, wrong area
            move(it, id = "a_dressing")       // everyday independence
        }
        val independent = badge("independent_me")
        assertTrue("five days in the right area should earn it", independent.isUnlocked)
        assertEquals(5, independent.progress)
    }

    @Test
    fun `an unlocked badge is never taken back by a later recount`() = runBlocking {
        formats.keys.forEach { move("2026-09-21", id = it) }
        assertTrue(badge("little_adventurer").isUnlocked)

        // A later event recomputes everything. The badge must survive it.
        move("2026-09-22", id = "a_quick")
        assertTrue("earned is earned", badge("little_adventurer").isUnlocked)
    }

    @Test
    fun `an unknown badge code sits at zero rather than crashing`() {
        assertEquals(0, AchievementEvaluator.progressFor("not_a_badge", AchievementEvaluator.Counters()))
    }
}
