package com.ableys.app.data.ledger

import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone

/**
 * Turns the days a child actually moved into the number the Move tab shows.
 *
 * Derived from the ledger rather than incremented in place. A counter that is only ever nudged
 * forward cannot be rebuilt after corruption, and this projection is the one the rest of the
 * ledger design promises is rebuildable -- so the rule is replayed over the stored days instead.
 * A year of movement is at most 365 rows of one short string; replaying it costs nothing.
 *
 * Dates arrive as the YYYY-MM-DD [ProgressEvent.localDay] already stamped in the family's own
 * timezone, so this class does civil-date arithmetic only and never touches a zone again.
 */
object StreakCalculator {

    /**
     * How long a run survives without movement. Two days means one missed day is forgiven: a
     * family that moves Wednesday, misses Thursday and moves Friday keeps what they built.
     * The spec's whole posture is that a therapy app must not punish a bad week, and a counter
     * that resets to zero the first time a child is ill is a counter parents switch off.
     */
    const val GRACE_DAYS = 2L

    /**
     * The run as it stands on [today].
     *
     * Note what this counts: days moved within the current unbroken run, not consecutive calendar
     * days. A family alternating days keeps advancing, which is the intended encouragement, and
     * is why the UI must not label the number "days in a row".
     */
    fun streakOn(movementDays: List<String>, today: String): Int {
        val todayEpoch = epochDay(today) ?: return 0
        val days = movementDays.mapNotNull(::epochDay)
            .filter { it <= todayEpoch } // a device whose clock ran fast must not seed the future
            .distinct()
            .sorted()
        if (days.isEmpty()) return 0

        var run = 0
        var previous: Long? = null
        for (day in days) {
            run = when (val gap = previous?.let { day - it }) {
                null -> 1
                0L -> run
                in 1L..GRACE_DAYS -> run + 1
                else -> 1
            }
            previous = day
        }

        // The run also has to still be alive. Movement four days ago built a run that has since
        // lapsed, and showing it would be a claim about this week that is not true.
        val sinceLast = todayEpoch - days.last()
        return if (sinceLast > GRACE_DAYS) 0 else run
    }

    /** Whether the child has already moved on [today], the check that silences a reminder. */
    fun movedOn(movementDays: List<String>, today: String): Boolean = movementDays.contains(today)

    /** The last [count] calendar days ending at [today], oldest first. */
    fun recentDays(today: String, count: Int): List<String> {
        val end = epochDay(today) ?: return emptyList()
        return ((end - count + 1)..end).map(::dateOf)
    }

    /**
     * Days since midnight UTC on 1970-01-01 for a YYYY-MM-DD civil date.
     *
     * UTC is used because both sides of every subtraction are civil dates, so a fixed zone makes
     * the arithmetic exact and DST-proof. The family's real timezone was applied once, when the
     * event was written.
     */
    private fun epochDay(date: String): Long? {
        val parts = date.split("-")
        if (parts.size != 3) return null
        val year = parts[0].toIntOrNull() ?: return null
        val month = parts[1].toIntOrNull() ?: return null
        val day = parts[2].toIntOrNull() ?: return null
        val calendar = GregorianCalendar(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(year, month - 1, day)
        }
        return calendar.timeInMillis / 86_400_000L
    }

    private fun dateOf(epochDay: Long): String {
        val calendar = GregorianCalendar(TimeZone.getTimeZone("UTC")).apply {
            clear()
            timeInMillis = epochDay * 86_400_000L
        }
        return String.format(
            Locale.US, "%04d-%02d-%02d",
            calendar.get(GregorianCalendar.YEAR),
            calendar.get(GregorianCalendar.MONTH) + 1,
            calendar.get(GregorianCalendar.DAY_OF_MONTH)
        )
    }
}
