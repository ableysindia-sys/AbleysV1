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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.TraceShape
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * A path drawn on screen for a fingertip to follow.
 *
 * Two jobs from one game. On a breathing activity the shape paces the breath -- a finger moving
 * up one side of a square while breathing in is easier for a child to sustain than a count. On a
 * pre-writing activity the same mechanic is the activity: the shapes here are the ones that come
 * before letters.
 *
 * Scoring is deliberately forgiving and never shows a failure. A child who traces loosely still
 * traced; a score that punishes a wobble teaches them that the wobble is the point, which is the
 * opposite of what this is for.
 */
@Composable
fun TracePathGame(
    shape: TraceShape,
    onComplete: (accuracyPercent: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val touched = remember { mutableStateListOf<Offset>() }
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    var covered by remember { mutableStateOf(0) }
    var finished by remember { mutableStateOf(false) }

    val guide = remember(shape, canvasSize) {
        if (canvasSize == Size.Zero) emptyList() else guidePoints(shape, canvasSize)
    }
    val hit = remember(guide) { MutableList(guide.size) { false } }

    Column(
        modifier = modifier.fillMaxWidth().testTag("trace_game_${shape.name.lowercase()}"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .pointerInput(shape, guide.size) {
                    val minSide = minOf(size.width, size.height).toFloat()
                    detectDragGestures(
                        onDragEnd = {
                            if (!finished && guide.isNotEmpty()) {
                                val pct = covered * 100 / guide.size
                                finished = true
                                onComplete(pct)
                            }
                        }
                    ) { change, _ ->
                        val p = change.position
                        touched.add(p)
                        // Mark any guide point the finger came close to. Tolerance is generous --
                        // this measures "did they follow it", not handwriting.
                        guide.forEachIndexed { i, g ->
                            if (!hit[i] && hypot(g.x - p.x, g.y - p.y) < minSide * 0.09f) {
                                hit[i] = true
                                covered++
                            }
                        }
                    }
                }
        ) {
            canvasSize = size
            val points = guidePoints(shape, size)

            // The path to follow, dashed so it reads as a guide rather than as a drawing.
            for (i in 0 until points.size - 1) {
                drawLine(
                    color = AbleySand,
                    start = points[i],
                    end = points[i + 1],
                    strokeWidth = 26f,
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(22f, 18f))
                )
            }

            // Guide points already covered, filled in as the finger passes them.
            points.forEachIndexed { i, p ->
                if (i < hit.size && hit[i]) {
                    drawCircle(color = AbleyTeal.copy(alpha = 0.5f), radius = 9f, center = p)
                }
            }

            // Start marker, so a child knows where to put their finger.
            points.firstOrNull()?.let {
                drawCircle(color = AbleyCoral, radius = 15f, center = it)
                drawCircle(color = Color.White, radius = 15f, center = it, style = Stroke(width = 4f))
            }

            // The child's own line.
            for (i in 0 until touched.size - 1) {
                val a = touched[i]
                val b = touched[i + 1]
                // Skip the jump when a finger lifts and lands somewhere else.
                if (hypot(b.x - a.x, b.y - a.y) < size.minDimension * 0.25f) {
                    drawLine(
                        color = AbleyCoral,
                        start = a,
                        end = b,
                        strokeWidth = 13f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = if (finished) "Nicely followed" else "Start on the red dot and follow the line",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (guide.isEmpty()) {
                ""
            } else {
                "${covered * 100 / guide.size}% of the path followed"
            },
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black.copy(alpha = 0.5f)
        )
    }
}

/** Evenly spaced points along the shape, used both to draw it and to score the trace. */
private fun guidePoints(shape: TraceShape, size: Size): List<Offset> {
    val w = size.width
    val h = size.height
    val pad = size.minDimension * 0.16f
    val out = mutableListOf<Offset>()

    fun line(from: Offset, to: Offset, steps: Int = 14) {
        for (i in 0..steps) {
            val t = i / steps.toFloat()
            out.add(Offset(from.x + (to.x - from.x) * t, from.y + (to.y - from.y) * t))
        }
    }

    when (shape) {
        TraceShape.SQUARE -> {
            val tl = Offset(pad, pad)
            val tr = Offset(w - pad, pad)
            val br = Offset(w - pad, h - pad)
            val bl = Offset(pad, h - pad)
            line(tl, tr); line(tr, br); line(br, bl); line(bl, tl)
        }
        TraceShape.TRIANGLE -> {
            val top = Offset(w / 2f, pad)
            val right = Offset(w - pad, h - pad)
            val left = Offset(pad, h - pad)
            line(top, right); line(right, left); line(left, top)
        }
        TraceShape.ZIGZAG -> {
            val steps = 6
            val span = (w - pad * 2) / steps
            var p = Offset(pad, h - pad)
            for (i in 1..steps) {
                val next = Offset(pad + span * i, if (i % 2 == 0) h - pad else pad)
                line(p, next, 10)
                p = next
            }
        }
        TraceShape.WAVE -> {
            val steps = 60
            for (i in 0..steps) {
                val t = i / steps.toFloat()
                val x = pad + (w - pad * 2) * t
                val y = h / 2f + sin(t * 4f * Math.PI).toFloat() * (h * 0.22f)
                out.add(Offset(x, y))
            }
        }
        TraceShape.SPIRAL -> {
            val steps = 90
            val maxR = size.minDimension / 2f - pad
            for (i in 0..steps) {
                val t = i / steps.toFloat()
                val angle = t * 3f * 2f * Math.PI
                val r = maxR * t
                out.add(
                    Offset(
                        w / 2f + (cos(angle) * r).toFloat(),
                        h / 2f + (sin(angle) * r).toFloat()
                    )
                )
            }
        }
        TraceShape.HAND -> {
            // Five fingers as up-and-over arches, which is the five-finger breath: up the thumb
            // on the way in, down the other side on the way out.
            val fingers = 5
            val span = (w - pad * 2) / fingers
            val base = h - pad
            for (f in 0 until fingers) {
                val x0 = pad + span * f
                val x1 = x0 + span
                val peak = pad + (if (f == 0 || f == 4) h * 0.18f else 0f)
                val steps = 16
                for (i in 0..steps) {
                    val t = i / steps.toFloat()
                    val x = x0 + (x1 - x0) * t
                    val y = base - (base - peak) * sin(t * Math.PI).toFloat()
                    out.add(Offset(x, y))
                }
            }
        }
    }
    // Drop near-duplicates so the covered-percentage is not dominated by corners.
    return out.filterIndexed { i, p ->
        i == 0 || abs(p.x - out[i - 1].x) > 1f || abs(p.y - out[i - 1].y) > 1f
    }
}
