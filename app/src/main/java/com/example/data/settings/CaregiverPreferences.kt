package com.example.data.settings

import android.content.Context
import com.example.data.content.PhysicalCues

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

    fun language(context: Context): PhysicalCues.Language {
        val stored = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, null)
        return PhysicalCues.Language.entries.firstOrNull { it.name == stored }
            ?: PhysicalCues.Language.ENGLISH
    }

    fun setLanguage(context: Context, language: PhysicalCues.Language) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, language.name)
            .apply()
    }
}
