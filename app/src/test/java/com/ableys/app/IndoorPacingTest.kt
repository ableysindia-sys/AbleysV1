package com.ableys.app

import com.ableys.app.data.content.AbleysContent
import com.ableys.app.data.content.IndoorPacing
import com.ableys.app.data.content.PlayModeAssignments
import com.ableys.app.data.content.RegulationActivities
import com.ableys.app.data.content.StrategyActivities
import com.ableys.app.data.model.SpaceNeeded
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Smog season in the north and the monsoon everywhere else take outdoor play away for weeks.
 * The indoor filter is only useful if the classification underneath it is right, so the shape of
 * the result is checked rather than assumed.
 */
class IndoorPacingTest {

    private val activities = PlayModeAssignments.apply(
        AbleysContent.moveActivities + RegulationActivities.activities + StrategyActivities.all
    )

    @Test
    fun mostOfTheCatalogueSurvivesAnIndoorDay() {
        val indoor = IndoorPacing.indoorOnly(activities)
        val ratio = indoor.size.toFloat() / activities.size
        println("indoor-safe: ${indoor.size} of ${activities.size} (${(ratio * 100).toInt()}%)")
        assertTrue(
            "Only ${(ratio * 100).toInt()}% survives an indoor day, which makes the filter a " +
                "dead end rather than a way through a bad-air week",
            ratio > 0.70f
        )
    }

    @Test
    fun activitiesThatNeedThrowingOrRunningAreNotOfferedAsIndoor() {
        val indoor = IndoorPacing.indoorOnly(activities).map { it.id }
        listOf("throw_catch_switch", "bear_crawl_relay").forEach { id ->
            if (activities.any { it.id == id }) {
                assertTrue("$id needs open space and must not appear on an indoor day", id !in indoor)
            }
        }
    }

    @Test
    fun breathingActivitiesNeedOnlyOneSpot() {
        val breathing = activities.filter { "breath" in it.id || "breathing" in it.title.lowercase() }
        assertTrue("Expected breathing activities in the catalogue", breathing.isNotEmpty())
        breathing.forEach {
            assertEquals(
                "${it.id} should need nothing more than a spot to sit",
                SpaceNeeded.SEATED_SPOT,
                it.spaceNeeded
            )
        }
    }

    @Test
    fun everyActivityGetsAClassification() {
        val counts = activities.groupingBy { it.spaceNeeded }.eachCount()
        println("space needed: $counts")
        assertEquals(activities.size, counts.values.sum())
        assertTrue("Every category should be represented", counts.keys.size >= 3)
    }
}
