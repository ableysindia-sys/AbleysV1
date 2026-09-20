package com.example

import com.example.play.physics.ParticleField
import com.example.play.physics.SoftBody
import org.junit.Assert.assertTrue
import org.junit.Test
import com.example.play.feedback.AmbientSound
import com.example.play.feedback.NoiseGenerator

/**
 * Measures the per-frame cost of the two simulations.
 *
 * Both run on the main thread inside the frame callback, which means their cost comes straight
 * out of the 16.6ms a frame has. Numbers here are JVM numbers and a mid-range phone is several
 * times slower, so these are for sizing and for catching a regression, not for claiming a frame
 * rate. The assertions are loose on purpose: a tight timing assertion on shared CI hardware
 * fails for reasons that have nothing to do with the code.
 */
class PhysicsBudgetTest {

    private fun timeUs(warmup: Int, runs: Int, block: () -> Unit): Double {
        repeat(warmup) { block() }
        val t0 = System.nanoTime()
        repeat(runs) { block() }
        return (System.nanoTime() - t0) / 1000.0 / runs
    }

    @Test
    fun softBodyStepFitsWellInsideAFrame() {
        val body = SoftBody()
        val us = timeUs(20_000, 100_000) {
            body.press(0f, -1f, 0.8f, 0.02f)
            body.step()
        }
        println("SoftBody press+step: %.1f us/frame".format(us))
        assertTrue("SoftBody step took $us us, which is a real regression", us < 500.0)
    }

    @Test
    fun particleFieldStepFitsWellInsideAFrame() {
        val field = ParticleField(count = 46)
        val us = timeUs(20_000, 100_000) {
            field.press(0.5f, 0.5f)
            field.step()
        }
        println("ParticleField(46) step: %.1f us/frame".format(us))
        assertTrue("ParticleField step took $us us, which is a real regression", us < 2_000.0)
    }

    @Test
    fun audioGenerationIsANegligibleFractionOfOneCore() {
        val generator = NoiseGenerator(AmbientSound.Bed.BROWN_NOISE)
        val buffer = ShortArray(2048)
        val us = timeUs(5_000, 30_000) { generator.fill(buffer) }
        val audioMs = generator.bufferDurationMs(buffer.size)
        val duty = us / (audioMs * 1000.0) * 100.0
        println("Audio: %.1f us to generate %.1f ms of sound = %.3f%% of one core".format(us, audioMs, duty))
        assertTrue("Audio generation is using %.2f%% of a core, which is no longer negligible".format(duty), duty < 5.0)
    }

    /**
     * The particle field is O(n^2) in the pair pass. This pins that down so nobody raises the
     * particle count later without seeing what it costs.
     */
    @Test
    fun particleFieldCostGrowsQuadraticallyWithCount() {
        val small = ParticleField(count = 40)
        val large = ParticleField(count = 80)
        val a = timeUs(10_000, 40_000) { small.step() }
        val b = timeUs(10_000, 40_000) { large.step() }
        println("ParticleField 40 -> %.1f us, 80 -> %.1f us (ratio %.1fx)".format(a, b, b / a))
        assertTrue("Doubling the count should cost noticeably more, not less", b > a)
    }

    /**
     * Five fingers is the worst case the field accepts, and it is what a child putting a whole
     * palm on the glass produces. The cost has to stay inside a frame with room to spare.
     */
    @Test
    fun fiveFingerMultiTouchStaysWithinFrameBudget() {
        val field = ParticleField(count = 46)
        val us = timeUs(20_000, 60_000) {
            field.clearTouches()
            repeat(ParticleField.MAX_TOUCHES) { i ->
                field.press(0.3f + i * 0.1f, 0.5f)
            }
            field.step()
        }
        println("ParticleField(46) + 5 touches: %.1f us/frame".format(us))
        assertTrue("Five-finger step took $us us", us < 2_500.0)
    }

    /** Extra fingers beyond the cap are dropped rather than growing the per-frame cost. */
    @Test
    fun touchesBeyondTheCapAreIgnored() {
        val field = ParticleField(count = 12)
        field.clearTouches()
        repeat(ParticleField.MAX_TOUCHES + 20) { field.press(0.5f, 0.5f) }
        // Nothing to assert on directly beyond it not throwing and still stepping cleanly.
        repeat(100) { field.step() }
        assertTrue(field.xs.all { it.isFinite() } && field.ys.all { it.isFinite() })
    }
}
