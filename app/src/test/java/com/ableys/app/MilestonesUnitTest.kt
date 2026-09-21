package com.ableys.app

import com.ableys.app.data.model.ChildDevelopmentMilestone
import com.ableys.app.data.model.MilestoneCategory
import com.ableys.app.data.model.MilestoneConverters
import com.ableys.app.data.model.MilestoneProgressStatus
import com.ableys.app.viewmodel.MilestonesStats
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MilestonesUnitTest {

    @Test
    fun milestoneEntity_creationAndDefaults() {
        val milestone = ChildDevelopmentMilestone(
            title = "Climbs stairs alternating feet",
            description = "Reciprocal foot movement without handrail",
            category = MilestoneCategory.MOTOR,
            progressStatus = MilestoneProgressStatus.ACHIEVED,
            date = "September 15, 2026",
            targetAgeMonths = 54,
            notes = "Mastered during playground session"
        )

        assertEquals("Climbs stairs alternating feet", milestone.title)
        assertEquals(MilestoneCategory.MOTOR, milestone.category)
        assertEquals(MilestoneProgressStatus.ACHIEVED, milestone.progressStatus)
        assertEquals("September 15, 2026", milestone.date)
        assertEquals(54, milestone.targetAgeMonths)
        assertEquals("Mastered during playground session", milestone.notes)
    }

    @Test
    fun milestoneCategories_haveDistinctDomains() {
        assertEquals("Motor", MilestoneCategory.MOTOR.displayName)
        assertEquals("Cognitive", MilestoneCategory.COGNITIVE.displayName)
        assertEquals("Speech", MilestoneCategory.SPEECH.displayName)

        assertEquals("🏃", MilestoneCategory.MOTOR.iconEmoji)
        assertEquals("🧩", MilestoneCategory.COGNITIVE.iconEmoji)
        assertEquals("💬", MilestoneCategory.SPEECH.iconEmoji)

        assertEquals(MilestoneCategory.MOTOR, MilestoneCategory.fromId("motor"))
        assertEquals(MilestoneCategory.COGNITIVE, MilestoneCategory.fromId("cognitive"))
        assertEquals(MilestoneCategory.SPEECH, MilestoneCategory.fromId("speech"))
    }

    @Test
    fun milestoneStatus_cycleProgression() {
        var status = MilestoneProgressStatus.NOT_STARTED
        assertEquals(MilestoneProgressStatus.NOT_STARTED, status)

        status = status.next()
        assertEquals(MilestoneProgressStatus.IN_PROGRESS, status)

        status = status.next()
        assertEquals(MilestoneProgressStatus.ACHIEVED, status)

        status = status.next()
        assertEquals(MilestoneProgressStatus.NOT_STARTED, status)
    }

    @Test
    fun milestoneConverters_serializeAndDeserializeCorrectly() {
        val converters = MilestoneConverters()

        // Category conversion
        val categoryStr = converters.fromCategory(MilestoneCategory.SPEECH)
        assertEquals("SPEECH", categoryStr)
        val deserializedCategory = converters.toCategory(categoryStr)
        assertEquals(MilestoneCategory.SPEECH, deserializedCategory)

        // Progress status conversion
        val statusStr = converters.fromProgressStatus(MilestoneProgressStatus.IN_PROGRESS)
        assertEquals("IN_PROGRESS", statusStr)
        val deserializedStatus = converters.toProgressStatus(statusStr)
        assertEquals(MilestoneProgressStatus.IN_PROGRESS, deserializedStatus)

        // Null safety
        assertNull(converters.fromCategory(null))
        assertNull(converters.toCategory(null))
        assertNull(converters.fromProgressStatus(null))
        assertNull(converters.toProgressStatus(null))
    }

    @Test
    fun milestoneStats_calculations() {
        val sampleMilestones = listOf(
            ChildDevelopmentMilestone(
                title = "Motor 1",
                description = "",
                category = MilestoneCategory.MOTOR,
                progressStatus = MilestoneProgressStatus.ACHIEVED,
                date = "2026-09-01"
            ),
            ChildDevelopmentMilestone(
                title = "Motor 2",
                description = "",
                category = MilestoneCategory.MOTOR,
                progressStatus = MilestoneProgressStatus.IN_PROGRESS,
                date = "2026-09-02"
            ),
            ChildDevelopmentMilestone(
                title = "Cognitive 1",
                description = "",
                category = MilestoneCategory.COGNITIVE,
                progressStatus = MilestoneProgressStatus.ACHIEVED,
                date = "2026-09-03"
            ),
            ChildDevelopmentMilestone(
                title = "Speech 1",
                description = "",
                category = MilestoneCategory.SPEECH,
                progressStatus = MilestoneProgressStatus.NOT_STARTED,
                date = "2026-09-04"
            )
        )

        val total = sampleMilestones.size
        val achieved = sampleMilestones.count { it.progressStatus == MilestoneProgressStatus.ACHIEVED }
        val inProgress = sampleMilestones.count { it.progressStatus == MilestoneProgressStatus.IN_PROGRESS }
        val notStarted = sampleMilestones.count { it.progressStatus == MilestoneProgressStatus.NOT_STARTED }

        val motorItems = sampleMilestones.filter { it.category == MilestoneCategory.MOTOR }
        val stats = MilestonesStats(
            totalCount = total,
            achievedCount = achieved,
            inProgressCount = inProgress,
            notStartedCount = notStarted,
            percentageAchieved = (achieved.toFloat() / total) * 100f,
            motorAchieved = motorItems.count { it.progressStatus == MilestoneProgressStatus.ACHIEVED },
            motorTotal = motorItems.size
        )

        assertEquals(4, stats.totalCount)
        assertEquals(2, stats.achievedCount)
        assertEquals(1, stats.inProgressCount)
        assertEquals(1, stats.notStartedCount)
        assertEquals(50.0f, stats.percentageAchieved, 0.01f)
        assertEquals(1, stats.motorAchieved)
        assertEquals(2, stats.motorTotal)
    }
}
