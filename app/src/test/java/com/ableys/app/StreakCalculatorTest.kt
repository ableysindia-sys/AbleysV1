package com.ableys.app

import com.ableys.app.data.ledger.MovementReminderWorker
import com.ableys.app.data.ledger.StreakCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StreakCalculatorTest {

    @Test
    fun `no movement is no run`() {
        assertEquals(0, StreakCalculator.streakOn(emptyList(), "2026-09-21"))
    }

    @Test
    fun `consecutive days accumulate`() {
        val days = listOf("2026-09-19", "2026-09-20", "2026-09-21")
        assertEquals(3, StreakCalculator.streakOn(days, "2026-09-21"))
    }

    @Test
    fun `moving twice in a day counts once`() {
        // Two sessions on the same date arrive as the same localDay string.
        val days = listOf("2026-09-20", "2026-09-21", "2026-09-21")
        assertEquals(2, StreakCalculator.streakOn(days, "2026-09-21"))
    }

    @Test
    fun `one missed day is forgiven`() {
        // Moved Sat and Sun, missed Mon, moved Tue.
        val days = listOf("2026-09-19", "2026-09-20", "2026-09-22")
        assertEquals("the run should survive a single gap", 3, StreakCalculator.streakOn(days, "2026-09-22"))
    }

    @Test
    fun `two missed days start over`() {
        val days = listOf("2026-09-19", "2026-09-20", "2026-09-23")
        assertEquals(1, StreakCalculator.streakOn(days, "2026-09-23"))
    }

    @Test
    fun `a run that has lapsed is not still displayed`() {
        // Last movement four days ago. The number must not claim a run that is over.
        val days = listOf("2026-09-15", "2026-09-16", "2026-09-17")
        assertEquals(0, StreakCalculator.streakOn(days, "2026-09-21"))
    }

    @Test
    fun `a run stays alive through the grace window without advancing`() {
        // Moved yesterday-but-one, nothing since. Still alive, still 2.
        val days = listOf("2026-09-18", "2026-09-19")
        assertEquals(2, StreakCalculator.streakOn(days, "2026-09-21"))
    }

    @Test
    fun `alternate-day movement keeps building`() {
        // Three sessions a week is a realistic therapy cadence and must not read as failure.
        val days = listOf("2026-09-14", "2026-09-16", "2026-09-18", "2026-09-20")
        assertEquals(4, StreakCalculator.streakOn(days, "2026-09-21"))
    }

    @Test
    fun `a clock that ran fast cannot seed the future`() {
        // A device set forward then corrected leaves a day ahead of today in the ledger.
        val days = listOf("2026-09-20", "2026-09-21", "2027-01-01")
        assertEquals(2, StreakCalculator.streakOn(days, "2026-09-21"))
    }

    @Test
    fun `month and year boundaries are real dates, not string arithmetic`() {
        assertEquals(3, StreakCalculator.streakOn(listOf("2026-02-27", "2026-02-28", "2026-03-01"), "2026-03-01"))
        assertEquals(2, StreakCalculator.streakOn(listOf("2025-12-31", "2026-01-01"), "2026-01-01"))
    }

    @Test
    fun `leap day is a day`() {
        assertEquals(3, StreakCalculator.streakOn(listOf("2028-02-28", "2028-02-29", "2028-03-01"), "2028-03-01"))
    }

    @Test
    fun `the reminder check is silent on a day already moved`() {
        val days = listOf("2026-09-20", "2026-09-21")
        assertTrue(StreakCalculator.movedOn(days, "2026-09-21"))
        assertFalse(StreakCalculator.movedOn(days, "2026-09-22"))
    }

    @Test
    fun `the week strip is seven real dates ending today`() {
        val week = StreakCalculator.recentDays("2026-03-01", 7)
        assertEquals(7, week.size)
        assertEquals("2026-02-23", week.first())
        assertEquals("2026-03-01", week.last())
    }

    @Test
    fun `reminder copy never names what was missed`() {
        val forbidden = listOf("lost", "missed", "broke", "failed", "behind", "don't", "didn't")
        listOf(0, 1, 6, 29, 40).forEach { streak ->
            val copy = MovementReminderWorker.copyFor("Aarav", streak).lowercase()
            forbidden.forEach { word ->
                assertFalse("copy for streak $streak used '$word': $copy", copy.contains(word))
            }
        }
        assertTrue(
            MovementReminderWorker.copyFor("Aarav", 6).contains("7 Day Explorer")
        )
    }
}
