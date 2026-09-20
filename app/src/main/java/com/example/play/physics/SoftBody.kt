package com.example.play.physics

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * A squishy blob: a ring of point masses held in shape by springs and internal pressure.
 *
 * Box2D and its ports are rigid-body engines. They are very good at boxes falling over and bad
 * at the one thing wanted here, which is the feel of pressing a thumb into putty and watching it
 * bulge out somewhere else. That behaviour comes from a soft body, and a soft body is a ring of
 * masses with three forces on it -- so this is about a hundred and fifty lines of Kotlin rather
 * than a dependency, and it runs at frame rate on a cheap phone.
 *
 * The three forces:
 *  - perimeter springs hold neighbouring points a fixed distance apart, so the outline stays
 *    an outline instead of unravelling;
 *  - radial springs pull each point back towards its resting distance from the centre, so the
 *    blob remembers its shape and returns to it;
 *  - pressure pushes outward along each point's normal in proportion to how much area has been
 *    lost, which is what makes a squeeze on one side bulge the other. Without it a press just
 *    makes a dent and nothing else moves, which reads as cloth rather than putty.
 */
class SoftBody(
    private val pointCount: Int = 28,
    private val restRadius: Float = 1f,
    private val stiffness: Float = 0.30f,
    private val radialStiffness: Float = 0.06f,
    private val pressure: Float = 0.55f,
    private val damping: Float = 0.88f
) {
    /**
     * Ceilings that keep explicit Euler integration honest.
     *
     * This solver is cheap and stable for gentle input and will run away on a hard one: a large
     * impulse stretches a spring, the stretched spring produces a larger impulse, and a few
     * frames later the blob is thousands of units wide. A child mashing the screen with a whole
     * palm is exactly that input, so both velocity and position are clamped rather than trusted.
     */
    private val maxSpeed = 0.35f
    private val maxRadius = restRadius * 2.2f
    val xs = FloatArray(pointCount)
    val ys = FloatArray(pointCount)
    private val vx = FloatArray(pointCount)
    private val vy = FloatArray(pointCount)

    /** Rest length between neighbouring perimeter points, from the circumference of the circle. */
    private val restEdge = 2f * restRadius * sin(PI.toFloat() / pointCount)
    private val restArea = PI.toFloat() * restRadius * restRadius

    init { reset() }

    fun reset() {
        for (i in 0 until pointCount) {
            val a = 2f * PI.toFloat() * i / pointCount
            xs[i] = cos(a) * restRadius
            ys[i] = sin(a) * restRadius
            vx[i] = 0f
            vy[i] = 0f
        }
    }

    /** Shoelace area of the current outline. Negative orientation is handled by the abs. */
    fun area(): Float {
        var a = 0f
        for (i in 0 until pointCount) {
            val j = (i + 1) % pointCount
            a += xs[i] * ys[j] - xs[j] * ys[i]
        }
        return abs(a) * 0.5f
    }

    /** 0 when at rest, approaching 1 when fully squashed. Drives the visuals and the haptics. */
    fun compression(): Float = ((restArea - area()) / restArea).coerceIn(0f, 1f)

    /**
     * Presses the blob at [px], [py] in body space. [radius] is the finger's reach and
     * [strength] how hard it pushes.
     */
    fun press(px: Float, py: Float, radius: Float, strength: Float) {
        for (i in 0 until pointCount) {
            val dx = xs[i] - px
            val dy = ys[i] - py
            val d = hypot(dx, dy)
            if (d < radius && d > 1e-4f) {
                val falloff = 1f - (d / radius)
                // Push the point away from the finger, which for a point inside the reach means
                // outward from the touch -- the dent forms because the pressure term then has to
                // put that lost area somewhere else.
                vx[i] -= (dx / d) * falloff * strength
                vy[i] -= (dy / d) * falloff * strength
            }
        }
    }

    /** Advances one step. [dt] is in frames rather than seconds; 1f is a normal frame. */
    fun step(dt: Float = 1f) {
        val currentArea = area().coerceAtLeast(1e-4f)
        val pressureTerm = (pressure * (restArea / currentArea - 1f)).coerceIn(-0.5f, 0.5f)

        for (i in 0 until pointCount) {
            val prev = (i - 1 + pointCount) % pointCount
            val next = (i + 1) % pointCount

            // Perimeter springs.
            accumulateSpring(i, next, restEdge, stiffness)
            accumulateSpring(i, prev, restEdge, stiffness)

            // Radial spring back towards the resting outline.
            val d = hypot(xs[i], ys[i])
            if (d > 1e-4f) {
                val pull = (restRadius - d) * radialStiffness
                vx[i] += (xs[i] / d) * pull
                vy[i] += (ys[i] / d) * pull

                // Pressure along the outward normal.
                vx[i] += (xs[i] / d) * pressureTerm
                vy[i] += (ys[i] / d) * pressureTerm
            }
        }

        for (i in 0 until pointCount) {
            vx[i] *= damping
            vy[i] *= damping

            val speed = hypot(vx[i], vy[i])
            if (speed > maxSpeed) {
                vx[i] = vx[i] / speed * maxSpeed
                vy[i] = vy[i] / speed * maxSpeed
            }

            xs[i] += vx[i] * dt
            ys[i] += vy[i] * dt

            // A point can still overshoot in one step; pull it back onto the outer limit and
            // drop its outward velocity so it settles instead of bouncing off the ceiling.
            val d = hypot(xs[i], ys[i])
            if (d > maxRadius && d > 1e-4f) {
                xs[i] = xs[i] / d * maxRadius
                ys[i] = ys[i] / d * maxRadius
                vx[i] *= 0.3f
                vy[i] *= 0.3f
            }
        }
    }

    private fun accumulateSpring(i: Int, j: Int, rest: Float, k: Float) {
        val dx = xs[j] - xs[i]
        val dy = ys[j] - ys[i]
        val d = hypot(dx, dy)
        if (d < 1e-4f) return
        val force = (d - rest) * k
        vx[i] += (dx / d) * force
        vy[i] += (dy / d) * force
    }
}
