package com.example

import com.example.data.content.AbleysContent
import com.example.data.content.PlayModeAssignments
import com.example.data.content.RegulationActivities
import com.example.data.content.StrategyActivities
import com.example.data.model.PlayMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The play-mode assignments live apart from the activity definitions so a content regeneration
 * cannot wipe them. The cost of that is they can silently point at ids that no longer exist,
 * which would show a parent a plain timer on an activity meant to have a game and give no error
 * anywhere. These tests are what makes that loud.
 */
class PlayModeAssignmentTest {

    private val raw = AbleysContent.moveActivities +
        RegulationActivities.activities +
        StrategyActivities.all

    private val applied = PlayModeAssignments.apply(raw)

    @Test
    fun everyAssignedIdStillExistsInTheCatalogue() {
        val known = raw.map { it.id }.toSet()
        val dangling = PlayModeAssignments.assignedIds - known
        assertTrue("Play modes assigned to ids that no longer exist: $dangling", dangling.isEmpty())
    }

    @Test
    fun assignmentsProduceTheExpectedNumberOfGames() {
        val gamed = applied.count { it.playMode != PlayMode.GUIDED_STEPS }
        assertEquals(PlayModeAssignments.assignedIds.size, gamed)
    }

    @Test
    fun everyBreathActivityCarriesAPattern() {
        val broken = applied
            .filter { it.playMode == PlayMode.BREATH_PACER && it.breathPattern == null }
            .map { it.id }
        assertTrue("Breath activities with no pattern: $broken", broken.isEmpty())
    }

    @Test
    fun everyTraceActivityCarriesAShape() {
        val broken = applied
            .filter { it.playMode == PlayMode.TRACE_PATH && it.traceShape == null }
            .map { it.id }
        assertTrue("Trace activities with no shape: $broken", broken.isEmpty())
    }

    @Test
    fun everySteadyHoldHasAHoldLongEnoughToBeWorthDoing() {
        val broken = applied
            .filter { it.playMode == PlayMode.STEADY_HOLD && it.holdSeconds < 10 }
            .map { "${it.id}=${it.holdSeconds}s" }
        assertTrue("Steady holds shorter than 10s: $broken", broken.isEmpty())
    }

    /** Young children and long breath-holds do not mix. */
    @Test
    fun noBreathPatternAsksAChildToHoldLongerThanThreeSeconds() {
        val tooLong = applied
            .mapNotNull { it.breathPattern?.let { p -> it.id to p } }
            .filter { (_, p) -> p.holdSeconds > 3 }
            .map { (id, p) -> "$id=${p.holdSeconds}s" }
        assertTrue("Breath holds too long for a young child: $tooLong", tooLong.isEmpty())
    }
}
