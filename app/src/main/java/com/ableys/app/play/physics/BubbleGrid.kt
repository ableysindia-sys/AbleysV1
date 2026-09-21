package com.ableys.app.play.physics

import kotlin.math.hypot

/**
 * The state of a grid of bubbles: which are popped, how far through their fade, and when a fresh
 * grid should arrive.
 *
 * Kept free of Compose types so it can be tested without a device. The timing constants are the
 * design -- a half-second fade, a pause after the last one, and a slow fade in -- so they live
 * somewhere a test can assert on them rather than inside a composable.
 */
class BubbleGrid(
    val columns: Int = 5,
    val rows: Int = 7,
    private val fadeOutMs: Long = 500,
    private val pauseAfterClearMs: Long = 700,
    private val fadeInMs: Long = 900,
    /** Hit radius as a multiple of the drawn radius. Above 1 so an imprecise tap still lands. */
    private val forgiveness: Float = 1.5f
) {
    val count: Int = columns * rows

    /** 0 means alive; otherwise the timestamp the bubble was popped. */
    private val poppedAt = LongArray(count)

    /** Timestamp the current grid finished fading in from. */
    private var grid0penedAt = 0L

    /** Set when the last bubble goes; the refill lands [pauseAfterClearMs] later. */
    private var clearedAt = 0L

    fun colOf(index: Int): Int = index % columns
    fun rowOf(index: Int): Int = index / columns

    /** How many bubbles are still unpopped. */
    fun remaining(): Int = poppedAt.count { it == 0L }

    /**
     * Opacity of bubble [index] at [nowMs]: 1 while alive, easing to 0 across the fade, and
     * rising again from 0 while a new grid arrives.
     */
    fun alphaOf(index: Int, nowMs: Long): Float {
        val popped = poppedAt[index]
        if (popped != 0L) {
            val elapsed = nowMs - popped
            if (elapsed >= fadeOutMs) return 0f
            return 1f - (elapsed.toFloat() / fadeOutMs)
        }
        // Alive. If the grid is still fading in, ramp up.
        val sinceOpen = nowMs - grid0penedAt
        if (grid0penedAt != 0L && sinceOpen < fadeInMs) {
            return (sinceOpen.toFloat() / fadeInMs).coerceIn(0f, 1f)
        }
        return 1f
    }

    /**
     * Pops the bubble nearest to the normalised point ([x], [y]) if it is within the forgiving
     * hit radius. Returns its index, or -1 when nothing was hit.
     */
    fun popAt(x: Float, y: Float, atMs: Long): Int {
        val cellW = 1f / columns
        val cellH = 1f / rows
        val radius = minOf(cellW, cellH) * 0.42f * forgiveness

        var best = -1
        var bestDistance = Float.MAX_VALUE
        for (i in 0 until count) {
            if (poppedAt[i] != 0L) continue
            if (alphaOf(i, atMs) < 0.35f) continue  // still fading in; not yet tappable
            val cx = (colOf(i) + 0.5f) * cellW
            val cy = (rowOf(i) + 0.5f) * cellH
            val d = hypot(x - cx, y - cy)
            if (d < radius && d < bestDistance) {
                bestDistance = d
                best = i
            }
        }
        if (best >= 0) {
            poppedAt[best] = atMs
            if (remaining() == 0) clearedAt = atMs
        }
        return best
    }

    /** Advances timing. Refills the grid once the last fade has finished and the pause is up. */
    fun update(nowMs: Long) {
        if (clearedAt != 0L && nowMs - clearedAt >= fadeOutMs + pauseAfterClearMs) {
            refill(nowMs)
        }
    }

    private fun refill(nowMs: Long) {
        poppedAt.fill(0L)
        clearedAt = 0L
        grid0penedAt = nowMs
    }
}
