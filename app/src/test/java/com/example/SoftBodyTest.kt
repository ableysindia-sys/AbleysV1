package com.example

import com.example.play.physics.SoftBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The soft body is the one piece of real simulation in the app, and a physics bug does not look
 * like a crash -- it looks like a blob that slowly inflates, drifts off screen, or collapses to
 * a dot. None of those throw, so they are caught here instead.
 */
class SoftBodyTest {

    @Test
    fun atRestItHoldsItsArea() {
        val body = SoftBody()
        val start = body.area()
        repeat(200) { body.step() }
        val drift = kotlin.math.abs(body.area() - start) / start
        assertTrue("Area drifted by ${drift * 100}% with no input", drift < 0.05f)
    }

    @Test
    fun aPressReducesAreaAndThenItRecovers() {
        val body = SoftBody()
        val rest = body.area()

        repeat(40) {
            body.press(px = 0f, py = -1f, radius = 0.8f, strength = 0.06f)
            body.step()
        }
        assertTrue("Pressing did not compress the body", body.area() < rest)
        assertTrue("Compression should register above zero", body.compression() > 0.01f)

        repeat(400) { body.step() }
        val recovered = kotlin.math.abs(body.area() - rest) / rest
        assertTrue("Body did not return to its resting shape (off by ${recovered * 100}%)", recovered < 0.10f)
    }

    @Test
    fun itNeverRunsAwayToInfinity() {
        val body = SoftBody()
        repeat(60) {
            body.press(px = 0.5f, py = 0.5f, radius = 1.5f, strength = 0.5f)
            body.step()
        }
        repeat(600) { body.step() }
        val finite = body.xs.all { it.isFinite() } && body.ys.all { it.isFinite() }
        assertTrue("Simulation produced NaN or infinity under a hard press", finite)
        val maxR = body.xs.indices.maxOf { kotlin.math.hypot(body.xs[it], body.ys[it]) }
        assertTrue("Body expanded without bound: max radius $maxR", maxR < 4f)
    }

    @Test
    fun compressionIsClampedToUnitRange() {
        val body = SoftBody()
        repeat(120) {
            body.press(px = 0f, py = 0f, radius = 2f, strength = 0.4f)
            body.step()
        }
        assertTrue(body.compression() in 0f..1f)
    }

    @Test
    fun resetRestoresTheStartingShape() {
        val body = SoftBody()
        val rest = body.area()
        repeat(50) {
            body.press(px = 0f, py = -1f, radius = 0.9f, strength = 0.08f)
            body.step()
        }
        body.reset()
        assertEquals(rest, body.area(), rest * 0.001f)
    }
}
