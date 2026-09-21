package com.ableys.app

import com.ableys.app.play.physics.BubbleGrid
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The bubble grid's timing constants are the design: a half-second fade, a pause, a slow refill,
 * and a hit target larger than the bubble. All four are the difference between soothing and
 * startling, so they are asserted rather than left to be noticed on a device.
 */
class BubbleGridTest {

    @Test
    fun aPoppedBubbleFadesOverHalfASecondRatherThanVanishing() {
        val grid = BubbleGrid()
        val index = grid.popAt(0.1f, 0.07f, atMs = 1_000)
        assertTrue("Expected a bubble under that point", index >= 0)

        assertEquals(1f, grid.alphaOf(index, 1_000), 0.02f)
        assertEquals(0.5f, grid.alphaOf(index, 1_250), 0.05f)
        assertEquals(0f, grid.alphaOf(index, 1_500), 0.02f)
        // And it stays gone rather than reappearing.
        assertEquals(0f, grid.alphaOf(index, 3_000), 0.001f)
    }

    @Test
    fun theHitTargetIsLargerThanTheBubbleSoAnImpreciseTapStillLands() {
        val grid = BubbleGrid(columns = 5, rows = 7)
        // Centre of the first cell is (0.1, 0.0714). The drawn radius is 0.42 of the cell;
        // this point sits outside that but inside the forgiving radius.
        val cellH = 1f / 7f
        val drawn = minOf(1f / 5f, cellH) * 0.42f
        val offCentre = grid.popAt(0.1f + drawn * 1.2f, cellH / 2f, atMs = 100)
        assertTrue("A tap just outside the drawn bubble should still pop it", offCentre >= 0)
    }

    @Test
    fun aTapInTheGapBetweenBubblesPopsNothing() {
        val grid = BubbleGrid(columns = 5, rows = 7)
        // A corner of a cell, well outside any forgiving radius.
        assertEquals(-1, grid.popAt(0.0f, 0.0f, atMs = 100))
    }

    @Test
    fun clearingTheGridRefillsItAfterAPauseWithNoCompletionState() {
        val grid = BubbleGrid(columns = 2, rows = 2)
        var t = 1_000L
        repeat(grid.count) { i ->
            val col = i % 2
            val row = i / 2
            val hit = grid.popAt((col + 0.5f) / 2f, (row + 0.5f) / 2f, atMs = t)
            assertTrue("Bubble $i should have popped", hit >= 0)
            t += 10
        }
        assertEquals(0, grid.remaining())

        // Still empty immediately after the last fade.
        grid.update(t + 500)
        assertEquals(0, grid.remaining())

        // Refilled once the fade and the pause have passed.
        grid.update(t + 500 + 700 + 20)
        assertEquals(grid.count, grid.remaining())
    }

    @Test
    fun aFreshGridFadesInRatherThanAppearing() {
        val grid = BubbleGrid(columns = 2, rows = 2)
        var t = 1_000L
        repeat(grid.count) { i ->
            grid.popAt((i % 2 + 0.5f) / 2f, (i / 2 + 0.5f) / 2f, atMs = t)
            t += 10
        }
        val refillAt = t + 500 + 700 + 20
        grid.update(refillAt)

        assertTrue("A new grid should start near invisible", grid.alphaOf(0, refillAt) < 0.1f)
        assertTrue("and be part way in after 450ms", grid.alphaOf(0, refillAt + 450) in 0.3f..0.7f)
        assertEquals("and be fully in after the fade", 1f, grid.alphaOf(0, refillAt + 900), 0.05f)
    }

    @Test
    fun aBubbleStillFadingInCannotBePopped() {
        val grid = BubbleGrid(columns = 2, rows = 2)
        var t = 1_000L
        repeat(grid.count) { i ->
            grid.popAt((i % 2 + 0.5f) / 2f, (i / 2 + 0.5f) / 2f, atMs = t)
            t += 10
        }
        val refillAt = t + 500 + 700 + 20
        grid.update(refillAt)
        // Barely visible: a tap here would feel like popping something that is not there yet.
        assertEquals(-1, grid.popAt(0.25f, 0.25f, atMs = refillAt + 40))
    }
}
