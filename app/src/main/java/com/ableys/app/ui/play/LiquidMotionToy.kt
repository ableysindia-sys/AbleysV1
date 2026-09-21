package com.ableys.app.ui.play

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.ableys.app.ui.theme.AbleySand
import com.ableys.app.play.physics.ParticleField

private const val PARTICLES = 46
private val Lavender = Color(0xFFB9A7E0)
private val SoftTeal = Color(0xFF6FB3AE)

/**
 * A jar of slow, thick liquid to push around. No score, no timer, nothing to fail.
 *
 * This is the one surface in the app with no goal attached, and that is the whole design. A
 * child who has run out of capacity for being asked to do things still needs somewhere to go,
 * and every other screen here asks for something. It matches what Abley's already sells in the
 * liquid fidget range, so the physical and the digital version of the same idea agree.
 *
 * The feel is the entire specification: heavy, slow, predictable. Damping is high enough that
 * nothing ever moves fast, gravity is barely present so the liquid drifts rather than falls,
 * and the touch pushes particles away gently instead of flinging them. Anything snappier stops
 * being calming and starts being a game.
 */
@Composable
fun LiquidMotionToy(
    modifier: Modifier = Modifier
) {
    val field = remember { ParticleField(count = PARTICLES) }
    var side by remember { mutableFloatStateOf(0f) }

    // Fixed-rate stepping, so the liquid moves at the same speed on a 60Hz phone and a 120Hz one.
    LaunchedEffect(field) {
        runFixedStepLoop(stepHz = 60) { field.step() }
    }

    Column(
        modifier = modifier.fillMaxWidth().testTag("liquid_motion_toy"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            field.clearTouches()
                            if (side > 0f) {
                                event.changes
                                    .filter { it.pressed }
                                    .forEach { field.press(it.position.x / side, it.position.y / side) }
                            }
                        }
                    }
                }
        ) {
            side = size.minDimension
            drawRoundRect(
                color = AbleySand.copy(alpha = 0.3f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(32f, 32f)
            )
            // Overlapping soft gradients read as one body of liquid rather than as dots.
            for (i in 0 until PARTICLES) {
                val c = if (i % 2 == 0) SoftTeal else Lavender
                val centre = Offset(field.xs[i] * size.width, field.ys[i] * size.height)
                val r = field.radii[i] * size.minDimension
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(c.copy(alpha = 0.40f), c.copy(alpha = 0f)),
                        center = centre,
                        radius = r
                    ),
                    radius = r,
                    center = centre,
                    blendMode = BlendMode.Plus
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Push it around for as long as you like.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Nothing to finish here.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black.copy(alpha = 0.35f)
        )
    }
}
