package com.example.data.wire

import com.squareup.moshi.JsonClass

/**
 * The wire format for everything a clinician can change without an app release.
 *
 * Today the catalogue is compiled into the APK as Kotlin objects, which has one enormous virtue
 * and one disqualifying flaw. The virtue is that it always works: no network, no latency, no
 * failure mode during a session in a flat with patchy 4G. The flaw is that a practitioner
 * correcting a step waits on a Play review, which is not a workflow anybody will tolerate twice.
 *
 * This format keeps the virtue and removes the flaw. A bundle is a whole, versioned, verifiable
 * snapshot of the catalogue that the app downloads once and then owns. Sessions read from local
 * storage, never from the network, so nothing a family is doing can be interrupted by a request.
 *
 * Structure and presentation are separated on purpose. Nothing here holds display text; every
 * user-visible string is a key into [strings], resolved per language at read time. That is what
 * makes adding Hindi a content release rather than a code change, and it means the clinical
 * hierarchy can be reviewed in one language while the translations are still in progress.
 */
@JsonClass(generateAdapter = true)
data class ContentBundle(
    /** Monotonic. The app keeps the highest version it has successfully validated. */
    val version: Int,
    val generatedAt: String,
    /** BCP-47 tags present in [strings]. The first is the fallback for a missing key. */
    val languages: List<String>,
    val programs: List<WireProgram> = emptyList(),
    val activities: List<WireActivity> = emptyList(),
    /** language -> key -> text. */
    val strings: Map<String, Map<String, String>> = emptyMap()
) {
    /** Resolves a key, falling back through the language list rather than showing a raw key. */
    fun text(key: String, language: String): String {
        strings[language]?.get(key)?.let { return it }
        languages.forEach { lang -> strings[lang]?.get(key)?.let { return it } }
        return key
    }
}

/** A multi-week plan. Holds no text of its own. */
@JsonClass(generateAdapter = true)
data class WireProgram(
    val id: String,
    val titleKey: String,
    val area: String,
    val sessions: List<WireSession> = emptyList(),
    val review: WireReview = WireReview()
)

/** One sitting. */
@JsonClass(generateAdapter = true)
data class WireSession(
    val id: String,
    val week: Int,
    val number: Int,
    val totalSeconds: Int,
    val equipmentSku: String? = null,
    val equipmentNameKey: String? = null,
    val householdAlternativeKey: String? = null,
    val safety: WireSafety = WireSafety(),
    val steps: List<WireStep> = emptyList()
)

/**
 * One step.
 *
 * [cueId] is the short physical direction spoken and shown large, and it is an id rather than a
 * string so the twenty-six directions stay a reviewable set instead of drifting into free text
 * per step. [instructionKey] is the written detail for whoever reads it.
 */
@JsonClass(generateAdapter = true)
data class WireStep(
    val id: String,
    val order: Int,
    val durationSeconds: Int,
    val cueId: String,
    val instructionKey: String,
    val tipKey: String? = null,
    val demonstration: WireDemonstration = WireDemonstration()
)

/**
 * The demonstration asset.
 *
 * Deliberately a URL rather than bytes. Video is the one payload that must never enter the APK:
 * a hundred short clips is a download families on metered data will not make, and the assets
 * change on a different cadence from the code. [loop] is true by default because a caregiver
 * mirroring a movement should never have to tap replay.
 */
@JsonClass(generateAdapter = true)
data class WireDemonstration(
    /**
     * none | video | hls | lottie
     *
     * "video" is a single progressive file and is the right default for these. The primary path
     * is a clip already on disk, downloaded on wifi ahead of the session, so adaptive bitrate is
     * solving a problem the design has already removed -- and for a fifteen-second demonstration
     * a manifest plus segment requests costs more round trips than it saves. "hls" exists for
     * the content where it genuinely pays: anything long enough that a family would start
     * watching before it finished downloading.
     */
    val kind: String = "none",
    val url: String? = null,
    val posterUrl: String? = null,
    val loop: Boolean = true,
    /**
     * Alternative encodings, smallest first. Pre-caching picks one by device and connection
     * rather than downloading every rendition, which is the cost adaptive streaming would
     * otherwise impose on a pass whose whole point is to happen once on wifi.
     */
    val renditions: List<WireRendition> = emptyList(),
    /** Per-language voiceover tracks, keyed by BCP-47 tag. */
    val voiceoverUrls: Map<String, String> = emptyMap()
)

/** One encoding of a demonstration. */
@JsonClass(generateAdapter = true)
data class WireRendition(
    val url: String,
    val heightPx: Int,
    val bytes: Long
)

/**
 * Safety, as flags and pictograms rather than a paragraph.
 *
 * The flags exist so the client can enforce a boundary rather than merely display one. A
 * paragraph can be scrolled past; [requiresAcknowledgment] cannot.
 */
@JsonClass(generateAdapter = true)
data class WireSafety(
    val doKey: String? = null,
    val dontKey: String? = null,
    val guidanceKey: String? = null,
    val pictogramDoUrl: String? = null,
    val pictogramDontUrl: String? = null,
    val requiresAcknowledgment: Boolean = true,
    val requiresAdultWithinReach: Boolean = true,
    val notForUnsupervisedUse: Boolean = true,
    val stopIfChildResists: Boolean = true
)

/** Who signed this off, and whether they have. */
@JsonClass(generateAdapter = true)
data class WireReview(
    /** occupational_therapy | psychology | sleep | speech_language | parenting */
    val discipline: String = "occupational_therapy",
    /** draft | in_review | approved */
    val state: String = "draft",
    val reviewerName: String? = null,
    val reviewedAt: String? = null,
    /** Document and page the content was extracted from. */
    val sourceRef: String? = null
)

/** A Move activity in the same shape. */
@JsonClass(generateAdapter = true)
data class WireActivity(
    val id: String,
    val titleKey: String,
    val targetArea: String,
    val durationSeconds: Int,
    val format: String,
    val descriptionKey: String,
    val stepKeys: List<String> = emptyList(),
    val equipmentSku: String? = null,
    val equipmentNameKey: String? = null,
    val householdAlternativeKey: String? = null,
    val spaceNeeded: String = "SMALL_ROOM",
    val playMode: String = "GUIDED_STEPS",
    val review: WireReview = WireReview()
)
