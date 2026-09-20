package com.example.ui.play

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
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
import com.example.ui.theme.AbleySand
import kotlin.math.hypot
import kotlin.random.Random

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
    val xs = remember { FloatArray(PARTICLES) }
    val ys = remember { FloatArray(PARTICLES) }
    val vxs = remember { FloatArray(PARTICLES) }
    val vys = remember { FloatArray(PARTICLES) }
    val radii = remember { FloatArray(PARTICLES) }
    var seeded by remember { mutableStateOf(false) }
    var touch by remember { mutableStateOf<Offset?>(null) }
    var side by remember { mutableFloatStateOf(0f) }

    if (!seeded) {
        val rng = Random(7)
        for (i in 0 until PARTICLES) {
            xs[i] = rng.nextFloat()
            ys[i] = rng.nextFloat()
            vxs[i] = 0f
            vys[i] = 0f
            radii[i] = 0.055f + rng.nextFloat() * 0.055f
        }
        seeded = true
    }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { }
            val t = touch
            for (i in 0 until PARTICLES) {
                // Barely-there gravity: a drift downward, not a fall.
                vys[i] += 0.00016f

                if (t != null && side > 0f) {
                    val tx = t.x / side
                    val ty = t.y / side
                    val dx = xs[i] - tx
                    val dy = ys[i] - ty
                    val d = hypot(dx, dy)
                    val reach = 0.30f
                    if (d < reach && d > 1e-4f) {
                        val push = (1f - d / reach) * 0.0024f
                        vxs[i] += (dx / d) * push
                        vys[i] += (dy / d) * push
                    }
                }

                // Soft separation, so the liquid keeps a body instead of collapsing to a point.
                for (j in i + 1 until PARTICLES) {
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

                // Viscosity. This number is the difference between gel and water.
                vxs[i] *= 0.936f
                vys[i] *= 0.936f

                xs[i] += vxs[i]
                ys[i] += vys[i]

                // Walls absorb rather than bounce. A bounce reads as energy; this should have none.
                val r = radii[i] * 0.5f
                if (xs[i] < r) { xs[i] = r; vxs[i] *= -0.24f }
                if (xs[i] > 1f - r) { xs[i] = 1f - r; vxs[i] *= -0.24f }
                if (ys[i] < r) { ys[i] = r; vys[i] *= -0.24f }
                if (ys[i] > 1f - r) { ys[i] = 1f - r; vys[i] *= -0.24f }
            }
        }
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
                    detectDragGestures(
                        onDragStart = { touch = it },
                        onDragEnd = { touch = null },
                        onDragCancel = { touch = null }
                    ) { change, _ -> touch = change.position }
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
                val centre = Offset(xs[i] * size.width, ys[i] * size.height)
                val r = radii[i] * size.minDimension
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
