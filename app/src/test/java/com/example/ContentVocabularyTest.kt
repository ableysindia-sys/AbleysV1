package com.example

import com.example.data.content.AbleysContent
import com.example.data.content.RegulationActivities
import com.example.data.content.StrategyActivities
import com.example.data.model.MoveActivity
import com.example.data.model.ReviewState
import com.example.data.model.TherapyProgram
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Guards the vocabulary rule for the whole catalogue.
 *
 * Abley's talks about sensory needs, regulation and practitioners. It does not name conditions,
 * claim to treat anything, or tell a parent what their child has. That rule is easy to hold
 * while writing ten activities by hand and easy to lose across ninety-eight from four different
 * sources, so it is enforced here instead of remembered.
 */
class ContentVocabularyTest {

    // Matched on word boundaries, not as substrings: "secure closures" and "obscure" are
    // ordinary English and must not trip a check meant to catch "cure".
    private val banned = listOf(
        "adhd", "autism", "autistic", "asperger", "diagnosis", "diagnose", "diagnosed",
        "disorder", "treatment", "treat your child", "cure", "symptom", "symptoms",
        "ptsd", "therapist-approved", "therapy-approved", "clinically proven", "medication"
    )

    private fun String.matchesAsWord(haystack: String): Boolean =
        Regex("\\b" + Regex.escape(this) + "\\b").containsMatchIn(haystack)

    private val allActivities: List<MoveActivity>
        get() = AbleysContent.moveActivities +
            RegulationActivities.activities +
            StrategyActivities.all

    private val allPrograms: List<TherapyProgram>
        get() = AbleysContent.therapyPrograms

    private fun MoveActivity.searchableText(): String =
        listOf(title, categoryBadge, description, targetArea, motorType)
            .plus(demonstrationSteps)
            .plus(targetTags)
            .joinToString(" ")
            .lowercase()

    private fun TherapyProgram.searchableText(): String =
        listOf(title, equipmentNeeded, safetyGuidance)
            .plus(steps.map { "${it.name} ${it.instruction} ${it.therapistTip}" })
            .joinToString(" ")
            .lowercase()

    @Test
    fun activityContent_usesNoConditionOrTreatmentLanguage() {
        val offenders = allActivities.flatMap { activity ->
            val text = activity.searchableText()
            banned.filter { it.matchesAsWord(text) }.map { "${activity.id}: '$it'" }
        }
        assertTrue(
            "Content must not name conditions or claim treatment. Found: $offenders",
            offenders.isEmpty()
        )
    }

    @Test
    fun programContent_usesNoConditionOrTreatmentLanguage() {
        val offenders = allPrograms.flatMap { program ->
            val text = program.searchableText()
            banned.filter { it.matchesAsWord(text) }.map { "${program.id}: '$it'" }
        }
        assertTrue(
            "Programs must not name conditions or claim treatment. Found: $offenders",
            offenders.isEmpty()
        )
    }

    /** Provenance is what the reviewing practitioner checks against. Every item needs it. */
    @Test
    fun everyActivityCarriesASourceReference() {
        val missing = allActivities.filter { it.sourceRef.isBlank() }.map { it.id }
        assertTrue("Activities missing sourceRef: $missing", missing.isEmpty())
    }

    /** Nothing may claim a review it has not had. */
    @Test
    fun noActivityClaimsApprovalBeforeAReviewerHasSignedIt() {
        val claimed = allActivities
            .filter { it.reviewState == ReviewState.APPROVED }
            .map { it.id }
        assertTrue(
            "These claim approval but no reviewer has signed them off yet: $claimed",
            claimed.isEmpty()
        )
    }
}
