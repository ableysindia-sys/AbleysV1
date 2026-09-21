package com.ableys.app

import com.ableys.app.data.content.AbleysContent
import com.ableys.app.data.content.HouseholdAlternatives
import com.ableys.app.data.content.PlayModeAssignments
import com.ableys.app.data.content.RegulationActivities
import com.ableys.app.data.content.StrategyActivities
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * A family that cannot buy the equipment today is the one most likely to close the app. Anything
 * gated behind a purchase has to offer a way to do it anyway, and that is checked here rather
 * than hoped for as the catalogue grows.
 */
class HouseholdAlternativeTest {

    private val activities = PlayModeAssignments.apply(
        AbleysContent.moveActivities + RegulationActivities.activities + StrategyActivities.all
    )

    @Test
    fun everyActivityGatedBehindAProductOffersAWayToDoItAnyway() {
        val gated = activities.filter { !it.equipmentSku.isNullOrBlank() }
        val missing = gated.filter { it.householdAlternative.isNullOrBlank() }.map { it.id }
        assertTrue("Activities requiring a purchase with no alternative: $missing", missing.isEmpty())
    }

    @Test
    fun everyProgramGatedBehindAProductOffersAWayToDoItAnyway() {
        val gated = AbleysContent.therapyPrograms.filter { it.equipmentSku.isNotBlank() }
        val missing = gated.filter {
            HouseholdAlternatives.forEquipment(it.equipmentNeeded).isNullOrBlank()
        }.map { it.id }
        assertTrue("Programs requiring a purchase with no alternative: $missing", missing.isEmpty())
    }

    @Test
    fun ceilingMountedEquipmentIsFlaggedAsNeedingInstallation() {
        assertTrue(HouseholdAlternatives.needsInstallation("Platform Swing"))
        assertTrue(HouseholdAlternatives.needsInstallation("Abley's Glider Swing"))
        assertFalse(HouseholdAlternatives.needsInstallation("Weighted lap pad"))
        assertFalse(HouseholdAlternatives.needsInstallation("Hand exercise putty"))
    }

    /**
     * A swing that fails is a different category of problem from an activity that is merely less
     * good, so no substitution may suggest hanging anything from a household fixture.
     */
    @Test
    fun noAlternativeSuggestsHangingSomethingFromAHouseholdFixture() {
        val unsafe = listOf("hang a", "hang the", "tie a bedsheet", "from the fan", "curtain rod as")
        val texts = activities.mapNotNull { it.householdAlternative } +
            listOfNotNull(HouseholdAlternatives.forEquipment("Platform Swing"))
        texts.forEach { text ->
            val lower = text.lowercase()
            unsafe.forEach { phrase ->
                // "do not hang" is the safety boundary being stated, which is the opposite.
                val bad = lower.contains(phrase) && !lower.contains("do not hang")
                assertFalse("Unsafe suggestion '$phrase' in: $text", bad)
            }
        }
    }

    @Test
    fun theSwingAlternativeStatesTheSafetyBoundaryExplicitly() {
        val text = HouseholdAlternatives.forEquipment("Platform Swing")
        assertNotNull(text)
        assertTrue(
            "The swing substitution must say what not to do: $text",
            text!!.lowercase().contains("do not hang")
        )
    }
}
