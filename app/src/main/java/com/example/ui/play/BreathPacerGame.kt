package com.example.ui.play

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.example.data.model.BreathPattern
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal
import kotlinx.coroutines.delay

private enum class BreathPhase(val label: String) {
    INHALE("Breathe in"),
    HOLD("Hold"),
    EXHALE("Breathe out")
}

/**
 * A shape that grows and shrinks at the pace of one breath.
 *
 * This is the one place a screen beats a parent counting out loud: a child can follow a moving
 * edge without holding a number in their head, and both people can watch the same thing instead
 * of one of them watching the other.
 *
 * The circle grows on the in-breath and shrinks on the out-breath, which is the way round that
 * matches what the chest does. It is slower coming down than going up whenever the pattern says
 * so, because a longer out-breath is the part that does the settling.
 */
@Composable
fun BreathPacerGame(
    pattern: BreathPattern,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var phase by remember { mutableStateOf(BreathPhase.INHALE) }
    var cycle by remember { mutableIntStateOf(0) }
    var secondsLeft by remember { mutableIntStateOf(pattern.inhaleSeconds) }
    var finished by remember { mutableStateOf(false) }

    // One suspending loop owns the whole sequence. Trying to drive this from recomposition is how
    // breath pacers end up drifting a second per cycle.
    LaunchedEffect(pattern) {
        repeat(pattern.cycles) { index ->
            cycle = index + 1

            phase = BreathPhase.INHALE
            for (s in pattern.inhaleSeconds downTo 1) { secondsLeft = s; delay(1000) }

            if (pattern.holdSeconds > 0) {
                phase = BreathPhase.HOLD
                for (s in pattern.holdSeconds downTo 1) { secondsLeft = s; delay(1000) }
            }

            phase = BreathPhase.EXHALE
            for (s in pattern.exhaleSeconds downTo 1) { secondsLeft = s; delay(1000) }
        }
        finished = true
        onComplete()
    }

    val target = when (phase) {
        BreathPhase.INHALE -> 1f
        BreathPhase.HOLD -> 1f
        BreathPhase.EXHALE -> 0.42f
    }
    val durationMs = when (phase) {
        BreathPhase.INHALE -> pattern.inhaleSeconds * 1000
        BreathPhase.HOLD -> 200
        BreathPhase.EXHALE -> pattern.exhaleSeconds * 1000
    }
    val scale by animateFloatAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = durationMs, easing = LinearEasing),
        label = "breath"
    )

    Column(
        modifier = modifier.fillMaxWidth().testTag("breath_pacer"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(228.dp)) {
                val centre = Offset(size.width / 2f, size.height / 2f)
                val maxRadius = size.minDimension / 2f - 10f

                // Outer ring: the size the breath is heading towards.
                drawCircle(
                    color = AbleySand,
                    radius = maxRadius,
                    center = centre,
                    style = Stroke(width = 2f)
                )

                val radius = maxRadius * scale.coerceIn(0.2f, 1f)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AbleyCoral.copy(alpha = 0.32f),
                            AbleyTeal.copy(alpha = 0.16f)
                        ),
                        center = centre,
                        radius = radius.coerceAtLeast(1f)
                    ),
                    radius = radius,
                    center = centre
                )
                drawCircle(
                    color = if (phase == BreathPhase.EXHALE) AbleyTeal else AbleyCoral,
                    radius = radius,
                    center = centre,
                    style = Stroke(width = 5f)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (finished) "Done" else phase.label,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                if (!finished) {
                    Text(
                        text = "$secondsLeft",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (phase == BreathPhase.EXHALE) AbleyTeal else AbleyCoral
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = if (finished) {
                "That was ${pattern.cycles} breaths together"
            } else {
                "Breath $cycle of ${pattern.cycles}"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Follow the circle. Nobody has to get it exactly right.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black.copy(alpha = 0.45f)
        )
    }
}
