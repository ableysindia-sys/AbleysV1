package com.example.ui.play

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.play.feedback.Haptics
import com.example.play.feedback.SoftTone
import com.example.play.physics.BubbleGrid

private val BubbleColours = listOf(
    Color(0xFF9FC9C5),  // soft teal
    Color(0xFFB9A7E0),  // lavender
    Color(0xFFE8C7B8),  // clay
    Color(0xFFCBD9A8)   // sage
)

/**
 * Endless bubbles to press, with nothing to win.
 *
 * The bubble-wrap mechanic earns its place here because the reward is entirely in the doing:
 * press, feel, hear, repeat. There is no score, no timer and no completion screen, and when the
 * grid empties a new one simply fades in, so there is never a moment that says stop.
 *
 * Three decisions carry the feel:
 *  - a bubble fades over half a second rather than bursting, because a flash is startling and a
 *    startle is the opposite of what a child comes to this screen for;
 *  - the hit target is half again the size of the bubble, so an imprecise tap still lands -- a
 *    child with motor difficulties should not have to aim;
 *  - the pitch of the tone shifts a little each time, so a long run of taps stays a texture
 *    rather than turning into a metronome.
 */
@Composable
fun BubblePopGame(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val grid = remember { BubbleGrid(columns = 5, rows = 7) }
    val tone = remember { SoftTone() }
    var now by remember { mutableLongStateOf(0L) }
    var side by remember { mutableFloatStateOf(0f) }

    DisposableEffect(tone) { onDispose { tone.release() } }

    LaunchedEffect(grid) {
        runFixedStepLoop(stepHz = 60) {
            now += 1000L / 60L
            grid.update(now)
        }
    }

    Column(
        modifier = modifier.fillMaxWidth().testTag("bubble_pop_game"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.78f)
                .pointerInput(grid) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (side <= 0f) continue
                            event.changes
                                .filter { it.pressed && it.previousPressed.not() }
                                .forEach { change ->
                                    val popped = grid.popAt(
                                        x = change.position.x / size.width,
                                        y = change.position.y / size.height,
                                        atMs = now
                                    )
                                    if (popped >= 0) {
                                        Haptics.play(context, Haptics.Cue.SQUEEZE, intensity = 0.55f)
                                        // Pitch drifts across the grid so neighbours differ.
                                        tone.play(pitchVariation = (popped % 7) * 0.05f - 0.15f)
                                    }
                                }
                        }
                    }
                }
        ) {
            side = size.minDimension
            val cellW = size.width / grid.columns
            val cellH = size.height / grid.rows
            val radius = minOf(cellW, cellH) * 0.42f

            for (i in 0 until grid.count) {
                val alpha = grid.alphaOf(i, now)
                if (alpha <= 0.01f) continue
                val centre = Offset(
                    (grid.colOf(i) + 0.5f) * cellW,
                    (grid.rowOf(i) + 0.5f) * cellH
                )
                val colour = BubbleColours[i % BubbleColours.size]
                // Soft edge: the gradient reaches zero alpha at the rim, so there is no outline
                // anywhere and nothing to read as a hard boundary.
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            colour.copy(alpha = 0.85f * alpha),
                            colour.copy(alpha = 0.55f * alpha),
                            colour.copy(alpha = 0f)
                        ),
                        center = centre,
                        radius = radius
                    ),
                    radius = radius,
                    center = centre
                )
                // A soft highlight, offset up and left, so each one reads as raised.
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.40f * alpha),
                            Color.White.copy(alpha = 0f)
                        ),
                        center = Offset(centre.x - radius * 0.28f, centre.y - radius * 0.30f),
                        radius = radius * 0.55f
                    ),
                    radius = radius * 0.55f,
                    center = Offset(centre.x - radius * 0.28f, centre.y - radius * 0.30f)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Press them for as long as you like.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black.copy(alpha = 0.45f)
        )
    }
}
