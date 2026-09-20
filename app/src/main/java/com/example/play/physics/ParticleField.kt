package com.example.play.physics

import kotlin.math.hypot
import kotlin.random.Random

/**
 * The liquid simulation, lifted out of the composable that used to hold it.
 *
 * It lived inline in LiquidMotionToy, which made it impossible to test or time -- the only way
 * to find out what it cost per frame was to run the app and watch. Out here it can be measured,
 * and the measurement is in PhysicsBudgetTest.
 *
 * Coordinates are normalised to 0..1 so the simulation does not care what size it is drawn at.
 *
 * The pair pass is O(n^2). At the count actually used that is cheap and clear, and a spatial
 * hash would be a lot of machinery for a jar of gel; the test pins the growth so raising the
 * count is a decision somebody makes on purpose.
 */
class ParticleField(
    val count: Int = 46,
    seed: Int = 7,
    private val gravity: Float = 0.00016f,
    private val viscosity: Float = 0.936f,
    private val wallAbsorb: Float = -0.24f
) {
    val xs = FloatArray(count)
    val ys = FloatArray(count)
    val radii = FloatArray(count)
    private val vxs = FloatArray(count)
    private val vys = FloatArray(count)

    /** Touch points in normalised space. Multi-touch: a child squeezing uses more than one. */
    private val touchX = FloatArray(MAX_TOUCHES)
    private val touchY = FloatArray(MAX_TOUCHES)
    private var touchCount = 0

    companion object {
        const val MAX_TOUCHES = 5
        private const val REACH = 0.30f
        private const val PUSH = 0.0024f
    }

    init {
        val rng = Random(seed)
        for (i in 0 until count) {
            xs[i] = rng.nextFloat()
            ys[i] = rng.nextFloat()
            radii[i] = 0.055f + rng.nextFloat() * 0.055f
        }
    }

    fun clearTouches() { touchCount = 0 }

    /** Adds a touch point for this frame. Extra points beyond [MAX_TOUCHES] are ignored. */
    fun press(x: Float, y: Float) {
        if (touchCount >= MAX_TOUCHES) return
        touchX[touchCount] = x
        touchY[touchCount] = y
        touchCount++
    }

    fun step() {
        for (i in 0 until count) {
            vys[i] += gravity

            for (t in 0 until touchCount) {
                val dx = xs[i] - touchX[t]
                val dy = ys[i] - touchY[t]
                val d = hypot(dx, dy)
                if (d < REACH && d > 1e-4f) {
                    val push = (1f - d / REACH) * PUSH
                    vxs[i] += (dx / d) * push
                    vys[i] += (dy / d) * push
                }
            }

            for (j in i + 1 until count) {
                val dx = xs[j] - xs[i]
                val dy = ys[j] - ys[i]
                val d = hypot(dx, dy)
                val minD = (radii[i] + radii[j]) * 0.62f
                if (d in 1e-4f..minD) {
                    val push = (minD - d) * 0.010f
                    vxs[i] -= (dx / d) * push
                    vys[i] -= (dy / d) * push
                    vxs[j] += (dx / d) * push
                    vys[j] += (dy / d) * push
                }
            }

            vxs[i] *= viscosity
            vys[i] *= viscosity
            xs[i] += vxs[i]
            ys[i] += vys[i]

            val r = radii[i] * 0.5f
            if (xs[i] < r) { xs[i] = r; vxs[i] *= wallAbsorb }
            if (xs[i] > 1f - r) { xs[i] = 1f - r; vxs[i] *= wallAbsorb }
            if (ys[i] < r) { ys[i] = r; vys[i] *= wallAbsorb }
            if (ys[i] > 1f - r) { ys[i] = 1f - r; vys[i] *= wallAbsorb }
        }
    }
}
