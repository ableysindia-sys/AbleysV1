package com.example

import com.example.data.wire.BundleExporter
import com.example.data.wire.ContentBundle
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Proves the wire schema against the catalogue that actually exists.
 *
 * A schema designed in the abstract fits the content somebody imagined. This one has to carry 98
 * activities and 25 programmes with their real provenance, review states, substitutions, cues
 * and safety boundaries, serialise, come back identical, and pass its own validator. If it
 * cannot, better to know before a CMS is built on it.
 */
class ContentBundleTest {

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val adapter = moshi.adapter(ContentBundle::class.java).indent("  ")

    private fun bundle() = BundleExporter.export(version = 1, generatedAt = "2026-09-21T00:00:00Z")

    @Test
    fun theWholeCatalogueFitsTheSchema() {
        val b = bundle()
        println("bundle: ${b.activities.size} activities, ${b.programs.size} programs, " +
            "${b.programs.sumOf { it.sessions.size }} sessions, " +
            "${b.strings["en"]?.size} english strings")
        assertEquals(98, b.activities.size)
        // Five focus areas, each a week 1-4 ladder of five sessions. This is the spec's
        // "Program (multi-week plan) -> Session (one sitting)" hierarchy, and asserting the
        // shape is what stops it collapsing back to one session per programme.
        assertEquals(5, b.programs.size)
        assertEquals(25, b.programs.sumOf { it.sessions.size })
        b.programs.forEach { p ->
            assertEquals("${p.id} should hold a full ladder", 5, p.sessions.size)
            assertEquals(
                "${p.id} sessions should be ordered by week",
                p.sessions.map { it.week },
                p.sessions.map { it.week }.sorted()
            )
        }
        assertTrue("Expected a substantial string table", (b.strings["en"]?.size ?: 0) > 500)
    }

    @Test
    fun theBundleValidatesCleanly() {
        val problems = BundleExporter.validate(bundle())
        assertTrue("Exported bundle failed its own validator: $problems", problems.isEmpty())
    }

    @Test
    fun itSurvivesARoundTripThroughJson() {
        val original = bundle()
        val json = adapter.toJson(original)
        val restored = adapter.fromJson(json)
        assertNotNull(restored)
        assertEquals(original.activities.size, restored!!.activities.size)
        assertEquals(original.programs.size, restored.programs.size)
        assertEquals(original.strings["en"]?.size, restored.strings["en"]?.size)
        // Spot-check that a deep field survived rather than just the counts.
        val step = restored.programs.first().sessions.first().steps.first()
        val source = original.programs.first().sessions.first().steps.first()
        assertEquals(source.cueId, step.cueId)
        assertEquals(source.durationSeconds, step.durationSeconds)
    }

    @Test
    fun noDisplayTextIsEmbeddedInTheStructure() {
        // The whole point of the split: structure holds keys, never sentences.
        val json = adapter.toJson(bundle())
        val structureOnly = json.substringBefore("\"strings\"")
        listOf("Breathe", "Sit close", "Stay within arm", "folded quilt").forEach { phrase ->
            assertTrue(
                "Display text '$phrase' leaked into the structural section",
                !structureOnly.contains(phrase)
            )
        }
    }

    @Test
    fun everyPhysicalCueUsedByAStepExistsInTheStringTable() {
        val b = bundle()
        val used = b.programs.flatMap { it.sessions }.flatMap { it.steps }.map { it.cueId }.toSet()
        used.forEach { cueId ->
            assertTrue(
                "cue.$cueId has no English string",
                b.strings["en"]?.containsKey("cue.$cueId") == true
            )
            assertTrue(
                "cue.$cueId has no Hindi string",
                b.strings["hi"]?.containsKey("cue.$cueId") == true
            )
        }
        println("distinct cues in use: ${used.size} of ${b.strings["hi"]?.keys?.count { it.startsWith("cue.") }}")
    }

    @Test
    fun missingTranslationsFallBackRatherThanShowingRawKeys() {
        val b = bundle()
        // Step instructions are English-only for now; Hindi must fall back, not print the key.
        val instructionKey = b.programs.first().sessions.first().steps.first().instructionKey
        val hindi = b.text(instructionKey, "hi")
        assertTrue("A missing translation printed its key", hindi != instructionKey)
        assertEquals(b.text(instructionKey, "en"), hindi)
    }

    @Test
    fun theValidatorRejectsADriftingSessionDuration() {
        val b = bundle()
        val broken = b.copy(
            programs = b.programs.mapIndexed { i, p ->
                if (i != 0) p else p.copy(
                    sessions = p.sessions.mapIndexed { j, s ->
                        if (j != 0) s else s.copy(totalSeconds = s.totalSeconds + 120)
                    }
                )
            }
        )
        val problems = BundleExporter.validate(broken)
        assertTrue("Validator missed a session whose steps no longer sum", problems.isNotEmpty())
    }

    /** Writes the bundle so it can be inspected and used to seed a CMS. */
    @Test
    fun exportsToDisk() {
        val out = File("build/ableys-content-v1.json")
        out.parentFile?.mkdirs()
        out.writeText(adapter.toJson(bundle()))
        println("wrote ${out.absolutePath} (${out.length() / 1024} KB)")
        assertTrue(out.length() > 10_000)
    }
}
