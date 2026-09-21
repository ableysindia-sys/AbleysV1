package com.example

import com.example.data.content.AbleysContent
import com.example.data.content.PhysicalCues
import com.example.ui.screens.support.SafetyLines
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The session player assumes the person running it may read little English and has both hands on
 * a child. These check the parts of that which can be checked without a device: that every step
 * resolves to a spoken direction, that every direction exists in both languages, and that every
 * programme has a do and a don't to show before it starts.
 */
class CaregiverAccessibilityTest {

    private val steps = AbleysContent.therapyPrograms.flatMap { it.steps }

    @Test
    fun everyTherapyStepResolvesToAPhysicalDirection() {
        val unresolved = steps.filter {
            PhysicalCues.forStep(it.name, it.instruction).id.isBlank()
        }
        assertTrue("Steps with no direction: ${unresolved.map { it.name }}", unresolved.isEmpty())
        assertEquals(134, steps.size)
    }

    @Test
    fun everyDirectionExistsInBothLanguagesAndInDevanagariAndRoman() {
        PhysicalCues.cues.forEach { cue ->
            assertTrue("${cue.id} has no English", cue.english.isNotBlank())
            assertTrue("${cue.id} has no Hindi", cue.hindi.isNotBlank())
            assertTrue("${cue.id} has no roman transliteration", cue.roman.isNotBlank())
            assertTrue(
                "${cue.id} Hindi should be in Devanagari",
                cue.hindi.any { it.code in 0x0900..0x097F }
            )
        }
    }

    @Test
    fun directionsAreShortEnoughToReadWhileHoldingAChild() {
        PhysicalCues.cues.forEach { cue ->
            assertTrue(
                "'${cue.english}' is too long to glance at (${cue.english.length} chars)",
                cue.english.length <= 16
            )
            assertTrue(
                "'${cue.roman}' is too long to glance at",
                cue.roman.length <= 20
            )
        }
    }

    @Test
    fun theDirectionsAreDirectionsRatherThanExplanations() {
        // A clinical explanation in this slot defeats the whole point of it.
        val clinical = listOf("vestibular", "proprioceptive", "regulation", "integration", "bilateral")
        PhysicalCues.cues.forEach { cue ->
            val text = "${cue.english} ${cue.roman}".lowercase()
            clinical.forEach { word ->
                assertTrue("'${cue.english}' explains rather than directs", !text.contains(word))
            }
        }
    }

    @Test
    fun everyProgramHasADoAndADontToShowBeforeStarting() {
        AbleysContent.therapyPrograms.forEach { program ->
            val doLine = SafetyLines.doFor(program)
            val dontLine = SafetyLines.dontFor(program)
            assertNotNull(doLine)
            assertNotNull(dontLine)
            assertTrue("${program.id} do-line is empty", doLine.isNotBlank())
            assertTrue("${program.id} dont-line is empty", dontLine.isNotBlank())
            assertTrue("${program.id} do and dont are the same", doLine != dontLine)
        }
    }

    @Test
    fun suspendedEquipmentGetsTheSupervisionBoundaryRatherThanTheGenericOne() {
        val swingPrograms = AbleysContent.therapyPrograms.filter {
            "swing" in it.equipmentNeeded.lowercase() || "swing" in it.title.lowercase()
        }
        swingPrograms.forEach { program ->
            assertTrue(
                "${program.id} should warn about leaving the child alone",
                SafetyLines.dontFor(program).lowercase().contains("alone")
            )
        }
    }
}
