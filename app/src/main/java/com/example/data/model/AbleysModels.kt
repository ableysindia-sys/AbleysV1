package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "child_profiles")
data class ChildProfile(
    @PrimaryKey val id: String = "child_default",
    val name: String = "Aarav",
    val age: Int = 5,
    val birthMonth: String = "September",
    val avatarEmoji: String = "🦁",
    val photoUri: String? = null,
    val supportLayerEnabled: Boolean = true,
    /** Exactly one profile is active at a time; the switcher moves this flag. */
    val isActive: Boolean = true,
    // False until a parent has completed onboarding for this child. The seeded demo child is the
    // only profile that starts with figures already on it; a real child starts at zero.
    val isOnboarded: Boolean = false,
    val totalXp: Int = 1240,
    val level: Int = 7,
    val currentStreak: Int = 12,
    val minutesMoved: Int = 248,
    val activeDays: Int = 18,
    val lastActiveTimestamp: Long = System.currentTimeMillis()
)

enum class SkillArea(
    val id: String,
    val displayName: String,
    val shortDescription: String,
    val iconName: String,
    // Maps to the live ableys.in catalogue tag so equipment and collections join on data, not by hand.
    val storeTag: String
) {
    CALM_COMFORT("calm_comfort", "Calm & Comfort", "Settling, regulation and recovering from big feelings", "heart", "need:calming-comfort"),
    FOCUS_ATTENTION("focus_attention", "Focus & Attention", "Looking carefully, remembering and staying with a task", "lightbulb", "need:focus-attention"),
    MOVEMENT_ENERGY("movement_energy", "Movement & Energy", "Balance, coordination and burning off restless energy", "run", "need:movement-energy"),
    STRENGTH_BODY_AWARENESS("strength_body_awareness", "Strength & Body Awareness", "Core strength, heavy work and knowing where the body is", "muscle", "need:strength-body-awareness"),
    HANDS_FINE_MOTOR("hands_fine_motor", "Hands & Fine Motor", "Pinch and grip strength, scissors and early handwriting", "hand", "product:fine-motor"),
    EVERYDAY_INDEPENDENCE("everyday_independence", "Everyday Independence", "Dressing, mealtimes, hygiene and daily routines", "home", "need:independence-life-skills"),
    PLAYING_WITH_OTHERS("playing_with_others", "Playing With Others", "Turn-taking, shared play and joining in", "chat", "need:social-play")
}

@Entity(tableName = "skill_progress", primaryKeys = ["childId", "skillAreaId"])
data class SkillProgress(
    val skillAreaId: String,
    val currentLevel: Int = 1,
    val xpEarned: Int = 0,
    val gamesCompleted: Int = 0,
    val mastered: Boolean = false,
    val childId: String = "child_default"
)

/**
 * Which practitioner has to sign a piece of content off.
 *
 * The panel started as one occupational therapist. Content about worry, self-talk and thought
 * patterns is a psychologist's call rather than an OT's, and sleep is its own discipline, so
 * each item names the chair it has to go past instead of everything queueing behind one person.
 */
enum class ReviewDiscipline(val displayName: String, val shortLabel: String) {
    OCCUPATIONAL_THERAPY("Occupational therapist", "OT"),
    PSYCHOLOGY("Child psychologist", "Psychologist"),
    SLEEP("Sleep practitioner", "Sleep practitioner"),
    SPEECH_LANGUAGE("Speech and language therapist", "SLT"),
    PARENTING("Parenting practitioner", "Parenting practitioner")
}

/** Where an item has got to with its reviewer. Nothing ships claiming a review it has not had. */
enum class ReviewState {
    DRAFT,
    IN_REVIEW,
    APPROVED
}

/**
 * How an activity is played on screen.
 *
 * Most of this catalogue is physical and parent-led, and for those the screen's job is to pace
 * and get out of the way -- a stepping-stone run does not improve by becoming a game about
 * stepping stones. [GUIDED_STEPS] is therefore the default and the majority.
 *
 * The exceptions are the three where a screen genuinely adds something: a breath needs a pacer
 * to follow, a pre-writing shape needs a path to trace, and a balance hold needs something that
 * can actually measure whether the child is still.
 */
enum class PlayMode {
    /** Step list with a timer. The screen paces; the activity happens in the room. */
    GUIDED_STEPS,

    /** An expanding and contracting shape the child breathes with. */
    BREATH_PACER,

    /** A path traced with a fingertip, scored on how closely it is followed. */
    TRACE_PATH,

    /** Phone held or balanced on; the accelerometer measures stillness. */
    STEADY_HOLD,

    /** A soft body that squashes under a finger and bulges out elsewhere. */
    SQUEEZE,

    /** Slow thick liquid with no goal, no timer and nothing to fail. */
    SENSORY_TOY,

    /** A grid of bubbles that fade when pressed and quietly refill. Also endless. */
    BUBBLE_POP
}

/** Shapes the trace game can draw. */
enum class TraceShape {
    SQUARE,
    TRIANGLE,
    HAND,
    ZIGZAG,
    WAVE,
    SPIRAL
}

/**
 * Timing for one breath cycle, in seconds. A zero hold is a hold the child never notices,
 * which is what young children need -- the long holds in adult breathing scripts are the
 * first thing to go wrong with a four-year-old.
 */
data class BreathPattern(
    val inhaleSeconds: Int = 4,
    val holdSeconds: Int = 0,
    val exhaleSeconds: Int = 4,
    val cycles: Int = 5
) {
    val totalSeconds: Int get() = (inhaleSeconds + holdSeconds + exhaleSeconds) * cycles
}

enum class MoveFormat(val displayName: String, val badgeColorHex: Long) {
    QUICK("5-minute Energy Burst", 0xFFEE4A41),
    DAILY("Morning Movement", 0xFF1F7A74),
    ANYWHERE("Indoor Adventure", 0xFFEFEAE0),
    FOCUSED("Balance Challenge", 0xFFEFEAE0),
    ONE_WEEK("7-Day Coordination Challenge", 0xFFEE4A41),
    ONE_MONTH("30-Day Move & Grow", 0xFF1F7A74)
}

data class MoveActivity(
    val id: String,
    val title: String,
    val categoryBadge: String,
    val durationMinutes: Int,
    val targetArea: String,
    val motorType: String, // Gross motor, Fine motor, Bilateral, etc.
    val format: MoveFormat,
    val description: String,
    val demonstrationSteps: List<String>,
    val equipmentName: String? = null,
    val equipmentSku: String? = null,
    val targetTags: List<String> = emptyList(),
    val xpReward: Int = 40,
    /** Document and page this was extracted from, shown to the reviewing practitioner. */
    val sourceRef: String = "",
    val reviewDiscipline: ReviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
    val reviewState: ReviewState = ReviewState.DRAFT,
    val playMode: PlayMode = PlayMode.GUIDED_STEPS,
    val breathPattern: BreathPattern? = null,
    val traceShape: TraceShape? = null,
    /** Seconds of stillness a STEADY_HOLD activity asks for. */
    val holdSeconds: Int = 0,
    /** Squeezes a SQUEEZE activity asks for. */
    val targetSqueezes: Int = 0
)

/** One day of a multi-day Move programme, pointing at an activity in the catalogue. */
data class MoveProgramDay(
    val dayNumber: Int,
    val activityId: String,
    val focusLabel: String
)

/**
 * A multi-day Move container -- the spec's "Program or challenge" screen.
 *
 * Days reference activities by id rather than embedding them, so a programme is a running order
 * over the reviewed catalogue and never a second copy of the content that could drift from it.
 */
data class MoveProgram(
    val id: String,
    val title: String,
    val subtitle: String,
    val format: MoveFormat,
    val totalDays: Int,
    val description: String,
    val days: List<MoveProgramDay>,
    val accentColorHex: Long = 0xFFEE4A41
)

/**
 * A completed day. Absent rows mean not yet done, so a family can pick a programme back up
 * after a gap without the app deciding they failed it.
 */
@Entity(tableName = "move_program_progress", primaryKeys = ["childId", "programId", "dayNumber"])
data class MoveProgramDayProgress(
    val childId: String = "child_default",
    val programId: String,
    val dayNumber: Int,
    val completedAt: Long = System.currentTimeMillis()
)

enum class MemoryType {
    PARENT_ADDED,
    SYSTEM_GENERATED
}

enum class MemorySource {
    PARENT,
    ABLEY_AUTO
}

@Entity(tableName = "memories")
data class MemoryItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: String = "child_default",
    val title: String,
    val caption: String,
    val dateString: String, // e.g. "September 18, 2026"
    val timestamp: Long = System.currentTimeMillis(),
    val source: MemorySource = MemorySource.PARENT,
    val badgeTag: String = "Moment",
    val highlightColorHex: Long = 0xFFEE4A41,
    val iconEmoji: String = "✨",
    /** Local content URI of the parent's photo, copied into app storage. Null for auto memories. */
    val photoUri: String? = null
) {
    val type: MemoryType
        get() = if (source == MemorySource.ABLEY_AUTO) MemoryType.SYSTEM_GENERATED else MemoryType.PARENT_ADDED

    val emojiTag: String
        get() = iconEmoji
}

enum class TherapyArea(val displayName: String) {
    SENSORY_REGULATION("Sensory & Regulation"),
    GROSS_MOTOR("Gross Motor"),
    FINE_MOTOR("Fine Motor"),
    BALANCE_COORDINATION("Balance & Coordination"),
    DAILY_LIVING("Daily Living"),
    SPEECH_COMMUNICATION("Speech & Communication")
}

data class TherapySessionStep(
    val stepNumber: Int,
    val name: String,
    val durationMinutes: Int,
    val instruction: String,
    val therapistTip: String
)

data class TherapyProgram(
    val id: String,
    val title: String,
    val area: TherapyArea,
    val weekNumber: Int,
    val sessionNumber: Int,
    val totalMinutes: Int,
    val equipmentNeeded: String,
    val equipmentSku: String,
    val safetyGuidance: String,
    val steps: List<TherapySessionStep>,
    val xpReward: Int = 50,
    /** Document and page this was extracted from, shown to the reviewing practitioner. */
    val sourceRef: String = "",
    val reviewDiscipline: ReviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
    val reviewState: ReviewState = ReviewState.DRAFT
) {
    val durationMinutes: Int
        get() = totalMinutes

    val clinicalTarget: String
        get() = "${area.displayName} · Wk $weekNumber Ses $sessionNumber"

    val description: String
        get() = "Evidence-based home protocol for ${area.displayName.lowercase()}."

    val equipmentName: String?
        get() = if (equipmentNeeded.isBlank() || equipmentNeeded.equals("None", ignoreCase = true)) null else equipmentNeeded

    val parentTips: List<String>
        get() = steps.map { it.therapistTip }
}

data class ParentStory(
    val id: String,
    val topic: String,
    val title: String,
    val authorName: String,
    val authorRole: String, // e.g. "Mother of a 7-year-old"
    val excerpt: String,
    val fullStory: String,
    val practicalTips: List<String>,
    val helpfulCount: Int = 42,
    /**
     * True for scaffold content written to exercise the screen. Real parent stories require
     * sourcing, consent and review; until that exists the UI must label these as samples so
     * nobody reads them as a real family's words.
     */
    val isPlaceholder: Boolean = false
)

@Entity(tableName = "achievements", primaryKeys = ["childId", "code"])
data class Achievement(
    val code: String,
    val title: String,
    val description: String,
    val badgeSymbol: String, // "7", "30", "100", "365", "⭐", "500", "1Y", etc.
    val isUnlocked: Boolean = false,
    val progress: Int = 0,
    val maxProgress: Int = 100,
    val unlockedDate: String? = null,
    val childId: String = "child_default"
)

@Entity(tableName = "equipment_items")
data class EquipmentProduct(
    @PrimaryKey val sku: String,
    val name: String,
    val category: String, // "Move", "Therapy at Home", "Skills"
    val description: String,
    val benefits: String,
    val isOwned: Boolean = false,
    val priceString: String = "$34.00",
    val storeUrl: String = "https://ableys.in/store"
)

enum class ShareCardTheme {
    DARK_STRAVA,
    CORAL_PRIDE,
    SAND_EDITORIAL,
    TEAL_MOVEMENT
}

data class ShareCardData(
    val title: String,
    val bigNumber: String,
    val unitLabel: String,
    val statsSubtitle: String,
    val childName: String = "Aarav",
    val ageOrYear: String = "Age 5",
    val theme: ShareCardTheme = ShareCardTheme.DARK_STRAVA,
    val footerMessage: String = "KEEP GROWING →"
)

