package com.ableys.app.data.sync

import com.ableys.app.data.content.PhysicalCues
import com.ableys.app.data.wire.ContentBundle
import com.ableys.app.data.wire.WireReview

/**
 * The contract a downloaded bundle must satisfy before it is allowed to replace a trusted one.
 *
 * This runs on the device rather than only in CI because CI validates what CI was given. The
 * thing that actually reaches a family is whatever the CDN served, and between a green pipeline
 * and a phone there is a publish step, a cache, and a file that can be truncated mid-download.
 * The client checking again costs milliseconds and removes a whole class of ways a child ends up
 * in front of a broken session.
 *
 * On localisation, one deliberate departure from a stricter reading: a key is required in the
 * primary language only, and other languages fall back per key. Requiring every key in every
 * language sounds safer and is the reason half-translated apps never ship their second language
 * -- Hindi would be blocked until all 1,207 strings were done, so it would never start. Instead
 * a language declares itself complete, and completeness is reported rather than enforced.
 */
object ContentValidator {

    /** Coverage of one language against the primary, for release reporting. */
    data class Coverage(val language: String, val translated: Int, val total: Int) {
        val percent: Int get() = if (total == 0) 100 else translated * 100 / total
        val complete: Boolean get() = translated >= total
    }

    /** Disciplines a reviewer may sign off as. Anything else is a typo or a made-up role. */
    private val disciplines = setOf(
        "occupational_therapy", "psychology", "sleep", "speech_language", "parenting"
    )

    /**
     * Content claiming approval must name who approved it and when.
     *
     * This is the client half of the sign-off gate. A CMS can enforce its own workflow, but what
     * reaches a family is whatever the pipeline emitted, and "approved" with no reviewer behind
     * it is the single claim in this app that must never be takeable on trust. A programme that
     * says a professional reviewed it, with nothing recording which professional, is worse than
     * one that admits it is a draft.
     */
    private fun checkReview(review: WireReview, owner: String, problems: MutableList<String>) {
        if (review.discipline !in disciplines) {
            problems += "$owner: unknown review discipline '${review.discipline}'"
        }
        if (review.state !in setOf("draft", "in_review", "approved")) {
            problems += "$owner: unknown review state '${review.state}'"
        }
        if (review.state == "approved") {
            if (review.reviewerName.isNullOrBlank()) {
                problems += "$owner: claims approval with no reviewer recorded"
            }
            if (review.reviewedAt.isNullOrBlank()) {
                problems += "$owner: claims approval with no sign-off date"
            }
            if (review.sourceRef.isNullOrBlank()) {
                problems += "$owner: approved with no source reference to review against"
            }
        }
    }

    fun check(bundle: ContentBundle): List<String> {
        val problems = mutableListOf<String>()

        if (bundle.version <= 0) problems += "version must be positive"
        if (bundle.languages.isEmpty()) problems += "no languages declared"
        if (bundle.programs.isEmpty() && bundle.activities.isEmpty()) problems += "bundle is empty"

        val primary = bundle.languages.firstOrNull() ?: return problems + "no primary language"
        val base = bundle.strings[primary].orEmpty()
        if (base.isEmpty()) problems += "primary language '$primary' has no strings"

        fun requireKey(key: String?, owner: String, what: String) {
            if (key == null) {
                problems += "$owner: missing $what"
            } else if (key !in base) {
                problems += "$owner: $what key '$key' absent from '$primary'"
            }
        }

        bundle.activities.forEach { activity ->
            checkReview(activity.review, activity.id, problems)
            requireKey(activity.titleKey, activity.id, "title")
            requireKey(activity.descriptionKey, activity.id, "description")
            if (activity.durationSeconds <= 0) problems += "${activity.id}: non-positive duration"
            activity.stepKeys.forEachIndexed { i, key ->
                requireKey(key, activity.id, "step $i")
            }
            // An activity that names a product must offer a way to do it without one.
            if (!activity.equipmentSku.isNullOrBlank() && activity.householdAlternativeKey == null) {
                problems += "${activity.id}: gated behind a product with no household alternative"
            }
        }

        bundle.programs.forEach { program ->
            checkReview(program.review, program.id, problems)
            requireKey(program.titleKey, program.id, "title")
            if (program.sessions.isEmpty()) problems += "${program.id}: programme has no sessions"

            program.sessions.forEach { session ->
                // Integrity: the doses are graded and the arithmetic is the dose.
                val sum = session.steps.sumOf { it.durationSeconds }
                if (session.steps.isEmpty()) {
                    problems += "${session.id}: session has no steps"
                } else if (sum != session.totalSeconds) {
                    problems += "${session.id}: steps sum to ${sum}s, session declares ${session.totalSeconds}s"
                }

                // Safety: the boundary must exist, and it must be enforceable.
                requireKey(session.safety.doKey, session.id, "safety do-line")
                requireKey(session.safety.dontKey, session.id, "safety don't-line")
                requireKey(session.safety.guidanceKey, session.id, "safety guidance")
                if (!session.safety.requiresAcknowledgment) {
                    problems += "${session.id}: safety acknowledgment disabled; sessions may not skip it"
                }

                session.steps.forEach { step ->
                    requireKey(step.instructionKey, step.id, "instruction")
                    if (step.durationSeconds <= 0) problems += "${step.id}: non-positive duration"
                    if (PhysicalCues.cues.none { it.id == step.cueId }) {
                        problems += "${step.id}: cue '${step.cueId}' is not in the direction vocabulary"
                    }
                    val demo = step.demonstration
                    if (demo.kind != "none" && demo.url.isNullOrBlank()) {
                        problems += "${step.id}: demonstration declared '${demo.kind}' with no url"
                    }
                    if (demo.kind !in setOf("none", "video", "hls", "lottie")) {
                        problems += "${step.id}: unknown demonstration kind '${demo.kind}'"
                    }
                }
            }
        }

        return problems
    }

    /**
     * Every id this bundle publishes, for the stability check on rotation.
     *
     * A family part-way through a four-week programme has progress rows pointing at these. If a
     * republish drops one, their progress silently references content that no longer exists, and
     * the failure shows up as a screen that will not open rather than as anything anybody can
     * diagnose.
     */
    fun publishedIds(bundle: ContentBundle): Set<String> =
        bundle.activities.map { it.id }.toSet() +
            bundle.programs.map { it.id } +
            bundle.programs.flatMap { p -> p.sessions.map { it.id } } +
            bundle.programs.flatMap { p -> p.sessions.flatMap { s -> s.steps.map { it.id } } }

    /** Per-language translation coverage. Reported, never enforced. */
    fun coverage(bundle: ContentBundle): List<Coverage> {
        val primary = bundle.languages.firstOrNull() ?: return emptyList()
        val keys = bundle.strings[primary].orEmpty().keys
        return bundle.languages.map { language ->
            val strings = bundle.strings[language].orEmpty()
            Coverage(language, keys.count { strings[it]?.isNotBlank() == true }, keys.size)
        }
    }

    /**
     * Every media file the bundle references, for the background download pass.
     *
     * Returned as plain URLs rather than as a player-specific request so the download layer can
     * be chosen later without touching validation.
     */
    fun mediaUrls(bundle: ContentBundle): List<String> =
        bundle.programs
            .flatMap { it.sessions }
            .flatMap { it.steps }
            .flatMap { step ->
                val demo = step.demonstration
                listOfNotNull(demo.url, demo.posterUrl) +
                    demo.renditions.map { it.url } +
                    demo.voiceoverUrls.values
            }
            .filter { it.isNotBlank() }
            .distinct()
}
