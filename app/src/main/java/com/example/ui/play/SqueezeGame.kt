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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.play.feedback.Haptics
import com.example.play.physics.SoftBody
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal
import androidx.compose.runtime.mutableStateListOf

/**
 * A blob that squashes under a finger and bulges out somewhere else.
 *
 * The equipment activities in the catalogue -- putty, squeeze balls, spiky balls -- are hand
 * strengthening, and the physical item is the thing that does the work. This is not a substitute
 * for it. It is the version for the times there is no putty to hand, and it does the one thing
 * a screen can do here honestly: show cause and effect, and count.
 *
 * The blob runs on [SoftBody], and the haptic rises with how hard it is being pressed, so the
 * squeeze is felt as well as seen.
 */
@Composable
fun SqueezeGame(
    targetSqueezes: Int,
    onComplete: (count: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val body = remember { SoftBody() }
    val touches = remember { mutableStateListOf<Offset>() }
    var canvasPx by remember { mutableFloatStateOf(0f) }
    var squeezes by remember { mutableIntStateOf(0) }
    var wasSquashed by remember { mutableStateOf(false) }
    var compression by remember { mutableFloatStateOf(0f) }
    var finished by remember { mutableStateOf(false) }

    // Physics runs off the frame clock rather than a timer, so it stays in step with what is
    // drawn on a slow device instead of racing ahead of it.
    LaunchedEffect(Unit) {
        runFixedStepLoop(stepHz = 60) {
            if (canvasPx > 0f) {
                val half = canvasPx / 2f
                // Each finger presses independently, so a two-handed squeeze flattens the blob
                // from both sides instead of denting it once.
                touches.forEach { t ->
                    body.press(
                        px = (t.x - half) / half,
                        py = (t.y - half) / half,
                        radius = 0.75f,
                        strength = 0.055f
                    )
                }
            }
            body.step()
            compression = body.compression()

            // One squeeze is a press past the threshold followed by a release, so holding a
            // finger down does not tick the counter forever.
            if (!wasSquashed && compression > 0.22f) {
                wasSquashed = true
                Haptics.play(context, Haptics.Cue.SQUEEZE, intensity = compression.coerceIn(0.3f, 1f))
            } else if (wasSquashed && compression < 0.08f) {
                wasSquashed = false
                squeezes++
                if (squeezes >= targetSqueezes && !finished) {
                    finished = true
                    Haptics.play(context, Haptics.Cue.COMPLETE)
                    onComplete(squeezes)
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth().testTag("squeeze_game"),
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
                            touches.clear()
                            event.changes.filter { it.pressed }.forEach { touches.add(it.position) }
                        }
                    }
                }
        ) {
            canvasPx = size.minDimension
            val half = size.minDimension / 2f
            val cx = size.width / 2f
            val cy = size.height / 2f
            val scale = half * 0.72f

            val path = Path()
            for (i in body.xs.indices) {
                val x = cx + body.xs[i] * scale
                val y = cy + body.ys[i] * scale
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()

            drawCircle(color = AbleySand.copy(alpha = 0.35f), radius = half * 0.78f, center = Offset(cx, cy))
            drawPath(
                path = path,
                brush = Brush.radialGradient(
                    colors = listOf(
                        AbleyCoral.copy(alpha = 0.55f),
                        AbleyTeal.copy(alpha = 0.35f)
                    ),
                    center = Offset(cx, cy),
                    radius = half
                )
            )
            drawPath(path = path, color = AbleyCoral, style = Stroke(width = 5f))
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (finished) "That is $targetSqueezes squeezes" else "$squeezes of $targetSqueezes squeezes",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Press it, then let go. Both halves count.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black.copy(alpha = 0.5f)
        )
    }
}
