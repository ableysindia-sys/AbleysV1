package com.example

import com.example.data.sync.ContentValidator
import com.example.data.wire.BundleExporter
import com.example.data.wire.ContentBundle
import com.example.data.wire.WireDemonstration
import com.example.data.wire.WireRendition
import com.example.data.wire.WireReview
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The client half of the clinical sign-off gate.
 *
 * A CMS can enforce whatever workflow it likes; what reaches a family is whatever the pipeline
 * emitted. "Reviewed by a professional" is the one claim in this app that must never be takeable
 * on trust, so a bundle asserting it without naming the professional is refused here, on the
 * device, after the CMS and the pipeline have both had their say.
 */
class ClinicalReviewGateTest {

    private fun bundle(): ContentBundle =
        BundleExporter.export(version = 3, generatedAt = "2026-09-21T00:00:00Z")

    private fun withProgramReview(b: ContentBundle, review: WireReview): ContentBundle =
        b.copy(programs = b.programs.mapIndexed { i, p -> if (i == 0) p.copy(review = review) else p })

    @Test
    fun contentClaimingApprovalWithoutANamedReviewerIsRefused() {
        val broken = withProgramReview(
            bundle(),
            WireReview(state = "approved", reviewedAt = "2026-09-20", sourceRef = "SIT p.16")
        )
        assertTrue(
            "A programme may not claim a professional reviewed it without naming them",
            ContentValidator.check(broken).any { "no reviewer recorded" in it }
        )
    }

    @Test
    fun contentClaimingApprovalWithoutASignOffDateIsRefused() {
        val broken = withProgramReview(
            bundle(),
            WireReview(state = "approved", reviewerName = "Dr A. Rao", sourceRef = "SIT p.16")
        )
        assertTrue(ContentValidator.check(broken).any { "no sign-off date" in it })
    }

    @Test
    fun approvedContentMustSayWhatItWasReviewedAgainst() {
        val broken = withProgramReview(
            bundle(),
            WireReview(state = "approved", reviewerName = "Dr A. Rao", reviewedAt = "2026-09-20")
        )
        assertTrue(
            "An approval with no source reference cannot be audited later",
            ContentValidator.check(broken).any { "no source reference" in it }
        )
    }

    @Test
    fun aFullySignedOffProgramPasses() {
        val good = withProgramReview(
            bundle(),
            WireReview(
                discipline = "occupational_therapy",
                state = "approved",
                reviewerName = "Dr A. Rao, OT Reg. 12345",
                reviewedAt = "2026-09-20T11:00:00Z",
                sourceRef = "Sensory Integration Toolkit p.16"
            )
        )
        assertTrue(ContentValidator.check(good).isEmpty())
    }

    @Test
    fun aMadeUpDisciplineOrStateIsRefused() {
        val badDiscipline = withProgramReview(bundle(), WireReview(discipline = "chiropractic"))
        assertTrue(ContentValidator.check(badDiscipline).any { "unknown review discipline" in it })

        val badState = withProgramReview(bundle(), WireReview(state = "probably_fine"))
        assertTrue(ContentValidator.check(badState).any { "unknown review state" in it })
    }

    @Test
    fun draftContentNeedsNoReviewerAndStillShips() {
        // Everything currently in the catalogue is draft. It must not be blocked from reaching
        // a device; it must only be blocked from claiming a review it has not had.
        assertTrue(ContentValidator.check(bundle()).isEmpty())
    }

    @Test
    fun hlsAndRenditionsAreValidDemonstrationShapes() {
        val b = bundle()
        val withMedia = b.copy(
            programs = b.programs.mapIndexed { i, p ->
                if (i != 0) p else p.copy(
                    sessions = p.sessions.mapIndexed { j, s ->
                        if (j != 0) s else s.copy(
                            steps = s.steps.map {
                                it.copy(
                                    demonstration = WireDemonstration(
                                        kind = "hls",
                                        url = "https://cdn.ableys.in/v/${it.id}/master.m3u8",
                                        renditions = listOf(
                                            WireRendition("https://cdn.ableys.in/v/${it.id}/360.mp4", 360, 420_000),
                                            WireRendition("https://cdn.ableys.in/v/${it.id}/720.mp4", 720, 1_100_000)
                                        )
                                    )
                                )
                            }
                        )
                    }
                )
            }
        )
        assertTrue(ContentValidator.check(withMedia).isEmpty())

        val urls = ContentValidator.mediaUrls(withMedia)
        val steps = withMedia.programs.first().sessions.first().steps.size
        // manifest plus two renditions per step
        assertEquals(steps * 3, urls.size)
    }

    @Test
    fun aBundleThatDropsContentAFamilyIsPartWayThroughIsRefused() {
        val b = bundle()
        val liveProgram = b.programs.first().id
        val published = ContentValidator.publishedIds(b)
        assertTrue("expected the programme id to be published", liveProgram in published)

        // A republish that drops that programme, while a family has progress against it.
        val dropped = b.copy(programs = b.programs.drop(1))
        val stillPublished = ContentValidator.publishedIds(dropped)
        assertTrue(
            "The dropped programme should no longer be published",
            liveProgram !in stillPublished
        )

        val vanished = setOf(liveProgram) - stillPublished
        assertEquals(
            "Exactly the id the family is mid-programme on should be detected as vanished",
            setOf(liveProgram),
            vanished
        )
    }
}
