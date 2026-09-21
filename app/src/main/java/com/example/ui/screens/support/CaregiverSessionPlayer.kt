package com.example.ui.screens.support

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.content.PhysicalCues
import com.example.data.model.TherapyProgram
import com.example.play.feedback.Haptics
import com.example.play.feedback.SessionChimes
import com.example.ui.play.runFixedStepLoop
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleyInk
import com.example.ui.theme.AbleyIvory
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal
import androidx.compose.ui.platform.LocalContext
import kotlin.math.abs
import com.example.telemetry.CrashReporter

/**
 * The session player rebuilt for whoever is actually running the session.
 *
 * In urban Indian homes the daily session is often run by a grandparent or a didi rather than
 * the parent who installed the app, and that person is frequently spotting a moving child with
 * both hands. Every assumption the old player made -- that someone is reading a step title,
 * watching a digit count down, and tapping a small button -- fails in that room.
 *
 * So: the ring carries the pacing and there is no countdown to read; the direction is one short
 * bilingual imperative in very large type; the state changes are audible because eyes are on the
 * child; the controls own the bottom third of the screen; and the whole surface is swipeable,
 * because a caregiver who uses short-form video already knows that gesture.
 *
 * The demonstration loop is the piece that still needs a person. The spec calls for an animated
 * or filmed demonstration on every step, carrying the whole instructional load for a caregiver
 * with limited English. Nothing here can generate that footage, so the slot is drawn honestly
 * as a slot rather than filled with something pretending to be a demonstration.
 */
@Composable
fun CaregiverSessionPlayer(
    program: TherapyProgram,
    onDismiss: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val chimes = remember { SessionChimes() }

    LaunchedEffect(program.id) {
        CrashReporter.breadcrumb("session start ${program.id} w${program.weekNumber}s${program.sessionNumber}")
    }
    DisposableEffect(chimes) { onDispose { chimes.release() } }

    var language by remember { mutableStateOf(PhysicalCues.Language.ENGLISH) }
    var stepIndex by remember { mutableIntStateOf(0) }
    var elapsedInStep by remember { mutableIntStateOf(0) }
    var running by remember { mutableStateOf(true) }
    var finished by remember { mutableStateOf(false) }

    val steps = program.steps
    val step = steps.getOrNull(stepIndex)
    // The old player hardcoded 45 seconds for every step and ignored the durations the
    // programmes actually carry. These are graded doses; a four-minute step is four minutes.
    val stepSeconds = ((step?.durationMinutes ?: 1) * 60).coerceAtLeast(10)

    fun goTo(index: Int) {
        val clamped = index.coerceIn(0, steps.lastIndex)
        if (clamped != stepIndex) {
            stepIndex = clamped
            elapsedInStep = 0
            Haptics.play(context, Haptics.Cue.STEADY_TICK)
        }
    }

    LaunchedEffect(stepIndex, running, finished) {
        if (!running || finished) return@LaunchedEffect
        var tick = 0
        runFixedStepLoop(stepHz = 60) {
            tick++
            if (tick % 60 == 0) {
                elapsedInStep++
                // Halfway bell, so a caregiver pacing reps knows where they are without looking.
                if (elapsedInStep == stepSeconds / 2) chimes.play(SessionChimes.Cue.REP_DONE)
                if (elapsedInStep >= stepSeconds) {
                    if (stepIndex < steps.lastIndex) {
                        chimes.play(SessionChimes.Cue.TIMER_END)
                        stepIndex++
                        elapsedInStep = 0
                    } else {
                        chimes.play(SessionChimes.Cue.STEP_COMPLETE)
                        Haptics.play(context, Haptics.Cue.COMPLETE)
                        finished = true
                    }
                }
            }
        }
    }

    val progress = (elapsedInStep.toFloat() / stepSeconds).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(progress, tween(300), label = "ring")

    val cue = step?.let { PhysicalCues.forStep(it.name, it.instruction) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AbleyIvory)
            // Swipe anywhere. A caregiver holding a child is not going to find a small chevron,
            // and this is the gesture they already use all day in a video app.
            .pointerInput(steps.size) {
                var drag = 0f
                detectHorizontalDragGestures(
                    onDragStart = { drag = 0f },
                    onDragEnd = {
                        if (abs(drag) > 80f) goTo(stepIndex + if (drag < 0) 1 else -1)
                    }
                ) { _, amount -> drag += amount }
            }
            .testTag("caregiver_session_player")
    ) {
        // Header: who is where, kept to two short lines.
        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AbleySand)
                    .clickable { onDismiss() }
                    .testTag("session_close"),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Close session", tint = AbleyInk)
            }
            Spacer(modifier = Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = program.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1
                )
                Text(
                    text = "${stepIndex + 1} / ${steps.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = AbleyInk.copy(alpha = 0.55f)
                )
            }
            // Language toggle, on the player rather than buried in settings, because the person
            // who needs it is often not the person who set the app up.
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(22.dp))
                    .background(AbleyTeal.copy(alpha = 0.12f))
                    .border(1.dp, AbleyTeal.copy(alpha = 0.4f), RoundedCornerShape(22.dp))
                    .clickable {
                        language = if (language == PhysicalCues.Language.ENGLISH) {
                            PhysicalCues.Language.HINGLISH
                        } else {
                            PhysicalCues.Language.ENGLISH
                        }
                    }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .testTag("language_toggle")
            ) {
                Text(
                    text = if (language == PhysicalCues.Language.ENGLISH) "A / अ" else "अ / A",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = AbleyTeal
                )
            }
        }

        // The ring and the direction, taking the whole upper area.
        Box(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(300.dp)) {
                val stroke = 30f
                val inset = stroke / 2f + 6f
                val arcSize = Size(size.width - inset * 2, size.height - inset * 2)
                drawArc(
                    color = AbleySand,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                    topLeft = Offset(inset, inset),
                    size = arcSize
                )
                drawArc(
                    color = if (finished) AbleyTeal else AbleyCoral,
                    startAngle = -90f,
                    sweepAngle = 360f * (if (finished) 1f else animatedProgress),
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                    topLeft = Offset(inset, inset),
                    size = arcSize
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 40.dp)
            ) {
                if (finished) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = "Session complete",
                        tint = AbleyTeal,
                        modifier = Modifier.size(84.dp)
                    )
                } else {
                    Text(
                        text = cue?.let { PhysicalCues.label(it, language) } ?: "",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 38.sp
                        ),
                        textAlign = TextAlign.Center,
                        color = AbleyInk
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = step?.name.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = AbleyInk.copy(alpha = 0.6f)
                    )
                }
            }
        }

        // Honest about the missing piece rather than faking a demonstration.
        Text(
            text = "Demonstration video not yet filmed for this step",
            style = MaterialTheme.typography.bodySmall,
            color = AbleyInk.copy(alpha = 0.35f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        )

        // Controls own the bottom third. No precision required anywhere.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.33f)
                .testTag("session_controls")
        ) {
            ControlSlab(
                icon = if (running) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                label = if (running) "Pause" else "Play",
                background = AbleyCoral,
                onClick = { running = !running },
                modifier = Modifier.weight(2f).testTag("session_play_pause")
            )
            ControlSlab(
                icon = Icons.Filled.SkipNext,
                label = "Next",
                background = AbleyTeal,
                onClick = {
                    if (stepIndex < steps.lastIndex) {
                        goTo(stepIndex + 1)
                    } else {
                        chimes.play(SessionChimes.Cue.STEP_COMPLETE)
                        finished = true
                        onComplete()
                    }
                },
                modifier = Modifier.weight(1f).testTag("session_next")
            )
        }
    }
}

@Composable
private fun ControlSlab(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    background: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(background)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(52.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
    }
}
