package com.example

import com.example.data.sync.ContentValidator
import com.example.data.wire.BundleExporter
import com.example.data.wire.ContentBundle
import com.example.data.wire.WireDemonstration
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The contract that decides whether a downloaded bundle is allowed near a family.
 *
 * Each of these is a way a CMS publish can go wrong between a green pipeline and a phone, and
 * each one would otherwise show up as a child in front of a broken session rather than as an
 * error anybody sees.
 */
class ContentValidatorTest {

    private fun good(): ContentBundle =
        BundleExporter.export(version = 2, generatedAt = "2026-09-21T00:00:00Z")

    @Test
    fun theCurrentCatalogueSatisfiesTheContract() {
        val problems = ContentValidator.check(good())
        assertTrue("Shipping catalogue fails its own contract: $problems", problems.isEmpty())
    }

    @Test
    fun aSessionWhoseStepsNoLongerSumIsRejected() {
        val b = good()
        val broken = b.copy(
            programs = b.programs.mapIndexed { i, p ->
                if (i != 0) p else p.copy(
                    sessions = p.sessions.mapIndexed { j, s ->
                        if (j != 0) s else s.copy(totalSeconds = s.totalSeconds + 60)
                    }
                )
            }
        )
        val problems = ContentValidator.check(broken)
        assertTrue("Drifting dose accepted: $problems", problems.any { "sum to" in it })
    }

    @Test
    fun aSessionWithoutASafetyBoundaryIsRejected() {
        val b = good()
        val broken = b.copy(
            programs = b.programs.mapIndexed { i, p ->
                if (i != 0) p else p.copy(
                    sessions = p.sessions.mapIndexed { j, s ->
                        if (j != 0) s else s.copy(safety = s.safety.copy(dontKey = null))
                    }
                )
            }
        )
        assertTrue(ContentValidator.check(broken).any { "don't-line" in it })
    }

    @Test
    fun aPublishThatDisablesTheSafetyAcknowledgmentIsRejected() {
        val b = good()
        val broken = b.copy(
            programs = b.programs.mapIndexed { i, p ->
                if (i != 0) p else p.copy(
                    sessions = p.sessions.mapIndexed { j, s ->
                        if (j != 0) s else s.copy(
                            safety = s.safety.copy(requiresAcknowledgment = false)
                        )
                    }
                )
            }
        )
        assertTrue(
            "A bundle must not be able to switch the safety gate off",
            ContentValidator.check(broken).any { "acknowledgment disabled" in it }
        )
    }

    @Test
    fun aMissingStringKeyInThePrimaryLanguageIsRejected() {
        val b = good()
        val firstActivity = b.activities.first()
        val stripped = b.strings["en"].orEmpty().toMutableMap()
        stripped.remove(firstActivity.titleKey)
        val broken = b.copy(strings = b.strings + ("en" to stripped))
        assertTrue(ContentValidator.check(broken).any { "absent from 'en'" in it })
    }

    @Test
    fun aDemonstrationDeclaredWithoutAUrlIsRejected() {
        val b = good()
        val broken = b.copy(
            programs = b.programs.mapIndexed { i, p ->
                if (i != 0) p else p.copy(
                    sessions = p.sessions.mapIndexed { j, s ->
                        if (j != 0) s else s.copy(
                            steps = s.steps.mapIndexed { k, step ->
                                if (k != 0) step
                                else step.copy(demonstration = WireDemonstration(kind = "video"))
                            }
                        )
                    }
                )
            }
        )
        assertTrue(ContentValidator.check(broken).any { "with no url" in it })
    }

    @Test
    fun anUnknownPhysicalCueIsRejected() {
        val b = good()
        val broken = b.copy(
            programs = b.programs.mapIndexed { i, p ->
                if (i != 0) p else p.copy(
                    sessions = p.sessions.mapIndexed { j, s ->
                        if (j != 0) s else s.copy(
                            steps = s.steps.mapIndexed { k, step ->
                                if (k != 0) step else step.copy(cueId = "levitate")
                            }
                        )
                    }
                )
            }
        )
        assertTrue(ContentValidator.check(broken).any { "direction vocabulary" in it })
    }

    /**
     * A partly translated language must ship, not block. Requiring every key in every language
     * is why half-translated apps never release their second language at all.
     */
    @Test
    fun aPartlyTranslatedLanguageIsReportedRatherThanRejected() {
        val b = good()
        assertTrue("Partial Hindi should not fail the contract", ContentValidator.check(b).isEmpty())

        val coverage = ContentValidator.coverage(b).associateBy { it.language }
        println("coverage: " + coverage.values.joinToString { "${it.language} ${it.percent}%" })
        assertEquals(100, coverage.getValue("en").percent)
        val hindi = coverage.getValue("hi")
        assertTrue("Hindi should be partial, not empty", hindi.percent in 1..99)
        assertTrue("Hindi should not claim completeness", !hindi.complete)
    }

    @Test
    fun mediaUrlsAreCollectedForTheBackgroundDownloadPass() {
        val b = good()
        // Nothing is filmed yet, so the list is empty and that is the honest answer.
        assertEquals(emptyList<String>(), ContentValidator.mediaUrls(b))

        val withVideo = b.copy(
            programs = b.programs.mapIndexed { i, p ->
                if (i != 0) p else p.copy(
                    sessions = p.sessions.mapIndexed { j, s ->
                        if (j != 0) s else s.copy(
                            steps = s.steps.map {
                                it.copy(
                                    demonstration = WireDemonstration(
                                        kind = "video",
                                        url = "https://cdn.ableys.in/v/${it.id}.mp4",
                                        posterUrl = "https://cdn.ableys.in/v/${it.id}.jpg",
                                        voiceoverUrls = mapOf("hi" to "https://cdn.ableys.in/vo/${it.id}-hi.m4a")
                                    )
                                )
                            }
                        )
                    }
                )
            }
        )
        val urls = ContentValidator.mediaUrls(withVideo)
        val steps = withVideo.programs.first().sessions.first().steps.size
        assertEquals("video, poster and voiceover per step", steps * 3, urls.size)
    }
}
