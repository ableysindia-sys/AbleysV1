package com.ableys.app.data.settings

import android.content.Context
import com.ableys.app.data.content.PhysicalCues

/**
 * The caregiver's own settings, which belong to the phone rather than to any one child.
 *
 * Language is here and not in the database on purpose: the person running the session is often
 * not the person who set the app up. A grandparent or a didi who switches the cues to Hinglish
 * is telling the app about themselves, and that choice should hold for the next session and
 * every session after, not reset the moment the screen closes -- which is what it did, because
 * the toggle lived in a `remember` block.
 *
 * SharedPreferences rather than DataStore: this is one enum read once when a player opens, the
 * backing map is already in memory after first load, and the app has no other DataStore use to
 * amortise the dependency against.
 */
object CaregiverPreferences {

    private const val PREFS = "ableys_caregiver"
    private const val KEY_LANGUAGE = "cue_language"
    private const val KEY_REMINDERS = "reminders_enabled"
    private const val KEY_REMINDER_HOUR = "reminder_hour"

    /**
     * Late afternoon rather than morning. A reminder at 9am arrives while a family is getting
     * out of the door; the window where a movement break is actually possible is after school
     * and before dinner.
     */
    const val DEFAULT_REMINDER_HOUR = 17

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun language(context: Context): PhysicalCues.Language {
        val stored = prefs(context).getString(KEY_LANGUAGE, null)
        return PhysicalCues.Language.entries.firstOrNull { it.name == stored }
            ?: PhysicalCues.Language.ENGLISH
    }

    fun remindersEnabled(context: Context): Boolean =
        prefs(context).getBoolean(KEY_REMINDERS, true)

    fun setRemindersEnabled(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(KEY_REMINDERS, enabled).apply()
    }

    fun reminderHour(context: Context): Int =
        prefs(context).getInt(KEY_REMINDER_HOUR, DEFAULT_REMINDER_HOUR).coerceIn(0, 23)

    fun setReminderHour(context: Context, hour: Int) {
        prefs(context).edit().putInt(KEY_REMINDER_HOUR, hour.coerceIn(0, 23)).apply()
    }

    fun setLanguage(context: Context, language: PhysicalCues.Language) {
        prefs(context).edit().putString(KEY_LANGUAGE, language.name).apply()
    }
}
