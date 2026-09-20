package com.example.ui.play

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleyTeal
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * The finish animation: one star that grows, settles and stops.
 *
 * Reward animation in children's apps usually means flashing, confetti and a fanfare. For a
 * child who has just spent four minutes being helped to settle, that undoes the four minutes.
 * So this expands once over about three quarters of a second on a decelerating curve, holds,
 * and fades. No flashing, no sound, nothing that repeats.
 *
 * This is drawn rather than played from a Lottie file on purpose. Lottie is the right answer the
 * day a designer hands over animations -- adding the dependency now would ship a renderer with
 * nothing to render, and a Lottie file hand-authored as JSON would be worse than this. The swap
 * is one composable when the assets exist.
 */
@Composable
fun GentleReward(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    var started by remember { mutableStateOf(false) }
    LaunchedEffect(visible) { if (visible) started = true }

    val progress by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(durationMillis = 760, easing = LinearOutSlowInEasing),
        label = "reward"
    )

    if (!visible) return

    Box(
        modifier = modifier.size(132.dp).testTag("gentle_reward"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(132.dp)) {
            val centre = Offset(size.width / 2f, size.height / 2f)
            val maxR = size.minDimension / 2f - 6f

            // A ring that opens outward and thins as it goes, like a slow ripple.
            val ringR = maxR * progress
            if (ringR > 1f) {
                drawCircle(
                    color = AbleyTeal.copy(alpha = (1f - progress) * 0.5f),
                    radius = ringR,
                    center = centre,
                    style = Stroke(width = 3f + (1f - progress) * 5f)
                )
            }

            // Five-pointed star, drawn to a scale that eases into place and stays.
            val starR = maxR * 0.52f * progress
            if (starR > 1f) {
                val path = Path()
                val points = 5
                for (i in 0 until points * 2) {
                    val r = if (i % 2 == 0) starR else starR * 0.44f
                    val a = -PI.toFloat() / 2f + i * PI.toFloat() / points
                    val x = centre.x + cos(a) * r
                    val y = centre.y + sin(a) * r
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
                drawPath(path = path, color = AbleyCoral.copy(alpha = 0.22f + 0.5f * progress))
                drawPath(path = path, color = AbleyCoral, style = Stroke(width = 3f))
            }
        }
    }
}
