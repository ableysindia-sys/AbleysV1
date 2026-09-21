package com.example.data.wire

import com.example.data.content.AbleysContent
import com.example.data.content.HouseholdAlternatives
import com.example.data.content.PhysicalCues
import com.example.data.content.PlayModeAssignments
import com.example.data.content.RegulationActivities
import com.example.data.content.StrategyActivities
import com.example.data.model.MoveActivity
import com.example.data.model.TherapyProgram
import com.example.ui.screens.support.SafetyLines

/**
 * Turns the compiled catalogue into a [ContentBundle].
 *
 * This exists to prove the schema before anything is built on it. A schema designed in the
 * abstract fits the content somebody imagined; this one has to carry 98 activities and 25
 * programmes that already exist, with their real provenance, review states, substitutions and
 * safety lines, or it is wrong and better to find that out now.
 *
 * It is also the migration path. When the catalogue moves to a CMS, this is what seeds it, and
 * the app is already reading the format the CMS will emit.
 */
object BundleExporter {

    private const val EN = "en"
    private const val HI = "hi"

    fun export(version: Int, generatedAt: String): ContentBundle {
        val en = LinkedHashMap<String, String>()
        val hi = LinkedHashMap<String, String>()

        fun key(k: String, english: String, hindi: String? = null): String {
            en[k] = english
            if (hindi != null) hi[k] = hindi
            return k
        }

        val activities = PlayModeAssignments.apply(
            AbleysContent.moveActivities + RegulationActivities.activities + StrategyActivities.all
        )

        val wireActivities = activities.map { activity ->
            val stepKeys = activity.demonstrationSteps.mapIndexed { i, step ->
                key("activity.${activity.id}.step.$i", step)
            }
            WireActivity(
                id = activity.id,
                titleKey = key("activity.${activity.id}.title", activity.title),
                targetArea = activity.targetArea,
                durationSeconds = activity.durationMinutes * 60,
                format = activity.format.name,
                descriptionKey = key("activity.${activity.id}.description", activity.description),
                stepKeys = stepKeys,
                equipmentSku = activity.equipmentSku,
                equipmentNameKey = activity.equipmentName?.let {
                    key("activity.${activity.id}.equipment", it)
                },
                householdAlternativeKey = activity.householdAlternative?.let {
                    key("activity.${activity.id}.alternative", it)
                },
                spaceNeeded = activity.spaceNeeded.name,
                playMode = activity.playMode.name,
                review = WireReview(
                    discipline = activity.reviewDiscipline.name.lowercase(),
                    state = activity.reviewState.name.lowercase(),
                    sourceRef = activity.sourceRef.ifBlank { null }
                )
            )
        }

        // A Program in the spec is "a multi-week plan for one focus area", and that is exactly
        // what the corpus contains: five areas, each a week 1-4 ladder of five sessions with its
        // own title. Grouping by title instead produced 25 programmes of one session each, which
        // is the hierarchy collapsed rather than represented.
        val wirePrograms = AbleysContent.therapyPrograms.groupBy { it.area }.map { (area, group) ->
            val sorted = group.sortedWith(compareBy({ it.weekNumber }, { it.sessionNumber }))
            val first = sorted.first()
            WireProgram(
                id = slug(area.displayName),
                titleKey = key("program.${slug(area.displayName)}.title", area.displayName),
                area = area.name,
                review = WireReview(
                    discipline = first.reviewDiscipline.name.lowercase(),
                    state = first.reviewState.name.lowercase(),
                    sourceRef = first.sourceRef.ifBlank { null }
                ),
                sessions = sorted.map { program -> session(program, ::key) }
            )
        }

        // The 26 physical directions ship as strings too, so a reviewer edits them in the CMS
        // rather than in Kotlin -- which is the whole point of moving content out of the app.
        PhysicalCues.cues.forEach { cue ->
            key("cue.${cue.id}", cue.english, "${cue.hindi}  ·  ${cue.roman}")
        }

        return ContentBundle(
            version = version,
            generatedAt = generatedAt,
            languages = listOf(EN, HI),
            programs = wirePrograms,
            activities = wireActivities,
            strings = mapOf(EN to en, HI to hi)
        )
    }

    private fun session(
        program: TherapyProgram,
        key: (String, String, String?) -> String
    ): WireSession {
        val id = "${slug(program.title)}-w${program.weekNumber}s${program.sessionNumber}"
        return WireSession(
            id = id,
            week = program.weekNumber,
            number = program.sessionNumber,
            totalSeconds = program.totalMinutes * 60,
            equipmentSku = program.equipmentSku.ifBlank { null },
            equipmentNameKey = program.equipmentName?.let { key("$id.equipment", it, null) },
            householdAlternativeKey =
                HouseholdAlternatives.forEquipment(program.equipmentNeeded)?.let {
                    key("$id.alternative", it, null)
                },
            safety = WireSafety(
                doKey = key("$id.safety.do", SafetyLines.doFor(program), null),
                dontKey = key("$id.safety.dont", SafetyLines.dontFor(program), null),
                guidanceKey = key("$id.safety.guidance", program.safetyGuidance, null),
                requiresAcknowledgment = true,
                requiresAdultWithinReach = true,
                notForUnsupervisedUse =
                    HouseholdAlternatives.needsInstallation(program.equipmentNeeded),
                stopIfChildResists = true
            ),
            steps = program.steps.map { step ->
                WireStep(
                    id = "$id-step${step.stepNumber}",
                    order = step.stepNumber,
                    durationSeconds = step.durationMinutes * 60,
                    cueId = PhysicalCues.forStep(step.name, step.instruction).id,
                    instructionKey = key("$id.step${step.stepNumber}.instruction", step.instruction, null),
                    tipKey = key("$id.step${step.stepNumber}.tip", step.therapistTip, null),
                    demonstration = WireDemonstration(kind = "none", loop = true)
                )
            }
        )
    }

    private fun slug(s: String): String =
        s.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')

    /** Every activity's steps, for a client that has the bundle but not the compiled objects. */
    fun activitySteps(bundle: ContentBundle, activity: WireActivity, language: String): List<String> =
        activity.stepKeys.map { bundle.text(it, language) }

    /** Sanity checks a bundle before the app is allowed to replace the one it already trusts. */
    fun validate(bundle: ContentBundle): List<String> {
        val problems = mutableListOf<String>()
        if (bundle.version <= 0) problems += "version must be positive"
        if (bundle.languages.isEmpty()) problems += "no languages declared"
        if (bundle.activities.isEmpty() && bundle.programs.isEmpty()) problems += "bundle is empty"

        val fallback = bundle.languages.firstOrNull()
        val base = bundle.strings[fallback] ?: emptyMap()

        bundle.activities.forEach { a ->
            if (a.titleKey !in base) problems += "${a.id}: title key missing from $fallback"
            if (a.durationSeconds <= 0) problems += "${a.id}: non-positive duration"
        }
        bundle.programs.forEach { p ->
            p.sessions.forEach { s ->
                val sum = s.steps.sumOf { it.durationSeconds }
                if (s.steps.isNotEmpty() && sum != s.totalSeconds) {
                    problems += "${s.id}: steps sum to ${sum}s but session claims ${s.totalSeconds}s"
                }
                if (s.safety.doKey == null || s.safety.dontKey == null) {
                    problems += "${s.id}: missing a safety boundary"
                }
                s.steps.forEach { step ->
                    if (PhysicalCues.cues.none { it.id == step.cueId }) {
                        problems += "${step.id}: unknown cue '${step.cueId}'"
                    }
                }
            }
        }
        return problems
    }
}
