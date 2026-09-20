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
    val supportLayerEnabled: Boolean = true,
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

@Entity(tableName = "skill_progress")
data class SkillProgress(
    @PrimaryKey val skillAreaId: String,
    val currentLevel: Int = 1,
    val xpEarned: Int = 0,
    val gamesCompleted: Int = 0,
    val mastered: Boolean = false
)

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
    val xpReward: Int = 40
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
    val iconEmoji: String = "✨"
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
    val xpReward: Int = 50
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
    val helpfulCount: Int = 42
)

@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey val code: String,
    val title: String,
    val description: String,
    val badgeSymbol: String, // "7", "30", "100", "365", "⭐", "500", "1Y", etc.
    val isUnlocked: Boolean = false,
    val progress: Int = 0,
    val maxProgress: Int = 100,
    val unlockedDate: String? = null
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

