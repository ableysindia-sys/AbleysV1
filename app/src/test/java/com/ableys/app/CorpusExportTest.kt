package com.ableys.app

import com.ableys.app.data.content.AbleysContent
import com.ableys.app.data.content.AchievementCatalogue
import com.ableys.app.data.content.ParentStoryTopics
import com.ableys.app.data.content.PhysicalCues
import com.ableys.app.data.content.RegulationActivities
import com.ableys.app.data.content.StrategyActivities
import com.ableys.app.data.wire.BundleExporter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Writes the whole content corpus out as JSON that does not depend on this app existing.
 *
 * The corpus -- 98 activities, the therapy programmes and their session steps, the equipment
 * catalogue and the bilingual strings -- is the part of this repository that took the longest
 * to assemble and the only part that survives a change of language or framework intact. This
 * test exists so that value can leave the building: it runs the app's own wire exporter, checks
 * the result against the app's own validator, and writes files any other stack can read.
 *
 * It is a generator rather than an assertion suite, but it does assert the counts, because an
 * export that silently drops half the corpus is worse than no export at all.
 */
class CorpusExportTest {

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val outDir = File("build/corpus-export").apply { mkdirs() }

    private inline fun <reified T> writeList(name: String, items: List<T>) {
        val type = Types.newParameterizedType(List::class.java, T::class.java)
        val json = moshi.adapter<List<T>>(type).indent("  ").toJson(items)
        File(outDir, name).writeText(json)
    }

    @Test
    fun `export the corpus`() {
        // 1. The wire bundle: activities, therapy programmes, and the en/hi string table.
        val bundle = BundleExporter.export(version = 1, generatedAt = "2026-09-22T00:00:00Z")
        val problems = BundleExporter.validate(bundle)
        assertTrue("The app's own validator rejected the bundle: $problems", problems.isEmpty())

        val bundleJson = moshi.adapter(com.ableys.app.data.wire.ContentBundle::class.java)
            .indent("  ").toJson(bundle)
        File(outDir, "01-bundle.json").writeText(bundleJson)

        // 2. Catalogues the wire format does not carry.
        writeList("02-move-programs.json", AbleysContent.movePrograms)
        writeList("03-equipment.json", AbleysContent.equipmentCatalogue + RegulationActivities.equipment)
        writeList("04-achievements.json", AchievementCatalogue.locked)
        writeList("05-physical-cues.json", PhysicalCues.cues)
        writeList("06-parent-story-topics.json", ParentStoryTopics.topics)

        // 3. Counts, so a silent drop fails loudly rather than shipping a thin corpus.
        val activityCount =
            AbleysContent.moveActivities.size + RegulationActivities.activities.size + StrategyActivities.all.size
        assertEquals("activity count changed", activityCount, bundle.activities.size)
        assertTrue("no therapy programmes exported", bundle.programs.isNotEmpty())
        assertTrue("no strings exported", bundle.strings.isNotEmpty())

        val sessions = bundle.programs.sumOf { it.sessions.size }
        val steps = bundle.programs.sumOf { p -> p.sessions.sumOf { it.steps.size } }

        File(outDir, "00-manifest.txt").writeText(
            buildString {
                appendLine("Abley's content corpus export")
                appendLine("generated from commit: see git log; bundle version 1")
                appendLine()
                appendLine("01-bundle.json")
                appendLine("  activities            ${bundle.activities.size}")
                appendLine("  therapy programmes    ${bundle.programs.size}")
                appendLine("  sessions              $sessions")
                appendLine("  session steps         $steps")
                appendLine("  languages             ${bundle.languages.joinToString()}")
                bundle.strings.forEach { (lang, table) ->
                    appendLine("  strings[$lang]         ${table.size}")
                }
                appendLine("02-move-programs.json    ${AbleysContent.movePrograms.size} programmes, " +
                    "${AbleysContent.movePrograms.sumOf { it.days.size }} day entries")
                appendLine("03-equipment.json        " +
                    "${AbleysContent.equipmentCatalogue.size + RegulationActivities.equipment.size}")
                appendLine("04-achievements.json     ${AchievementCatalogue.locked.size}")
                appendLine("05-physical-cues.json    ${PhysicalCues.cues.size}")
                appendLine("06-parent-story-topics.json ${ParentStoryTopics.topics.size}")
                appendLine()
                appendLine("NOT IN THIS EXPORT, and why:")
                appendLine("  Demonstration media -- none exists. Every step's demonstration.kind is")
                appendLine("    \"none\". The corpus is text instructions only.")
                appendLine("  Milestones -- 15 rows are seeded inline in AbleysRepository with")
                appendLine("    fabricated achievement dates. They are demo data, not a developmental")
                appendLine("    reference, and should be re-sourced rather than ported.")
                appendLine("  Parent stories -- all flagged isPlaceholder; only the topic briefs are real.")
                appendLine()
                appendLine("REVIEW STATE: every activity and programme is DRAFT. Nothing in this corpus")
                appendLine("  has been clinically reviewed, and no UI built on it should claim otherwise.")
            }
        )
    }
}
