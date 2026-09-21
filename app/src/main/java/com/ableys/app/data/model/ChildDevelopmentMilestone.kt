package com.ableys.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

/**
 * Category for child developmental milestones.
 * Supports the primary domains: Motor, Cognitive, and Speech.
 */
enum class MilestoneCategory(
    val id: String,
    val displayName: String,
    val description: String,
    val iconEmoji: String
) {
    MOTOR(
        id = "motor",
        displayName = "Motor",
        description = "Gross and fine motor coordination, posture, strength, and bilateral balance",
        iconEmoji = "🏃"
    ),
    COGNITIVE(
        id = "cognitive",
        displayName = "Cognitive",
        description = "Problem solving, memory, sequential logic, pattern recognition, and focus",
        iconEmoji = "🧩"
    ),
    SPEECH(
        id = "speech",
        displayName = "Speech",
        description = "Language expression, articulation, vocabulary, comprehension, and conversation",
        iconEmoji = "💬"
    );

    companion object {
        fun fromId(id: String): MilestoneCategory =
            entries.find { it.id.equals(id, ignoreCase = true) } ?: MOTOR
    }
}

/**
 * Progression status of a developmental milestone.
 */
enum class MilestoneProgressStatus(
    val id: String,
    val displayName: String,
    val colorHex: Long
) {
    NOT_STARTED(
        id = "not_started",
        displayName = "Not Started",
        colorHex = 0xFF8A909A
    ),
    IN_PROGRESS(
        id = "in_progress",
        displayName = "In Progress",
        colorHex = 0xFFECA82B
    ),
    ACHIEVED(
        id = "achieved",
        displayName = "Achieved",
        colorHex = 0xFF1F7A74
    );

    fun next(): MilestoneProgressStatus = when (this) {
        NOT_STARTED -> IN_PROGRESS
        IN_PROGRESS -> ACHIEVED
        ACHIEVED -> NOT_STARTED
    }

    companion object {
        fun fromId(id: String): MilestoneProgressStatus =
            entries.find { it.id.equals(id, ignoreCase = true) } ?: NOT_STARTED
    }
}

/**
 * Room database entity representing a child development milestone.
 */
@Entity(tableName = "child_development_milestones")
data class ChildDevelopmentMilestone(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: String = "child_default",
    val title: String,
    val description: String,
    val category: MilestoneCategory,
    val progressStatus: MilestoneProgressStatus = MilestoneProgressStatus.NOT_STARTED,
    val date: String, // Date achieved or targeted, e.g. "Sep 20, 2026"
    val targetAgeMonths: Int = 60, // e.g. 60 months (5 years)
    val notes: String = "",
    val dateAchieved: String? = null,
    val updatedTimestamp: Long = System.currentTimeMillis()
)

/**
 * Room TypeConverters for MilestoneCategory and MilestoneProgressStatus.
 */
class MilestoneConverters {
    @TypeConverter
    fun fromCategory(category: MilestoneCategory?): String? = category?.name

    @TypeConverter
    fun toCategory(value: String?): MilestoneCategory? =
        value?.let { runCatching { MilestoneCategory.valueOf(it) }.getOrNull() }

    @TypeConverter
    fun fromProgressStatus(status: MilestoneProgressStatus?): String? = status?.name

    @TypeConverter
    fun toProgressStatus(value: String?): MilestoneProgressStatus? =
        value?.let { runCatching { MilestoneProgressStatus.valueOf(it) }.getOrNull() }
}
