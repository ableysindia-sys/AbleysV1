package com.ableys.app

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ableys.app.data.ledger.MovementReminderWorker
import com.ableys.app.data.settings.CaregiverPreferences
import com.ableys.app.ui.components.hourLabel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Calendar
import java.util.TimeZone

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ReminderSettingsTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private fun at(hour: Int, minute: Int = 0): Long =
        Calendar.getInstance().apply {
            set(2026, Calendar.SEPTEMBER, 21, hour, minute, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    @Test
    fun `reminders are on by default but can be turned off`() {
        assertTrue(CaregiverPreferences.remindersEnabled(context))
        CaregiverPreferences.setRemindersEnabled(context, false)
        assertFalse(CaregiverPreferences.remindersEnabled(context))
    }

    @Test
    fun `the default hour is late afternoon, not the middle of the morning rush`() {
        assertEquals(17, CaregiverPreferences.reminderHour(context))
    }

    @Test
    fun `an out-of-range hour is clamped rather than stored`() {
        CaregiverPreferences.setReminderHour(context, 99)
        assertEquals(23, CaregiverPreferences.reminderHour(context))
        CaregiverPreferences.setReminderHour(context, -4)
        assertEquals(0, CaregiverPreferences.reminderHour(context))
    }

    @Test
    fun `the first run lands on the chosen hour, not on whenever the app was installed`() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"))
        try {
            // Installed at 2am. A bare periodic request would fire at 2am forever.
            val delay = MovementReminderWorker.millisUntilHour(17, at(2))
            assertEquals("15 hours from 2am to 5pm", 15 * 3_600_000L, delay)
        } finally {
            TimeZone.setDefault(null)
        }
    }

    @Test
    fun `an hour already past today waits for tomorrow rather than firing immediately`() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"))
        try {
            val delay = MovementReminderWorker.millisUntilHour(17, at(20))
            assertEquals("21 hours from 8pm to 5pm tomorrow", 21 * 3_600_000L, delay)
        } finally {
            TimeZone.setDefault(null)
        }
    }

    @Test
    fun `exactly on the hour waits for the next day rather than firing twice`() {
        val delay = MovementReminderWorker.millisUntilHour(17, at(17))
        assertEquals(24 * 3_600_000L, delay)
    }

    @Test
    fun `the hour reads the way a time of day is spoken`() {
        assertEquals("5 PM", hourLabel(17))
        assertEquals("12 AM", hourLabel(0))
        assertEquals("12 PM", hourLabel(12))
        assertEquals("9 AM", hourLabel(9))
    }
}
