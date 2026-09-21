package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.content.PhysicalCues
import com.example.data.settings.CaregiverPreferences
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class CaregiverPreferencesTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun `English until someone chooses otherwise`() {
        assertEquals(PhysicalCues.Language.ENGLISH, CaregiverPreferences.language(context))
    }

    @Test
    fun `a Hinglish choice outlives the player screen`() {
        CaregiverPreferences.setLanguage(context, PhysicalCues.Language.HINGLISH)
        assertEquals(PhysicalCues.Language.HINGLISH, CaregiverPreferences.language(context))
    }

    @Test
    fun `a stored value the app no longer recognises falls back rather than crashing`() {
        context.getSharedPreferences("ableys_caregiver", Context.MODE_PRIVATE)
            .edit().putString("cue_language", "MARATHI").apply()
        assertEquals(
            "an enum renamed or removed in a later version must not crash a session",
            PhysicalCues.Language.ENGLISH,
            CaregiverPreferences.language(context)
        )
    }
}
