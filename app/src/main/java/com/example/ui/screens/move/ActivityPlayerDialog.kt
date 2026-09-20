package com.example.ui.screens.move

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.MoveActivity
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleyCoralLight
import com.example.ui.theme.AbleyInk
import com.example.ui.theme.AbleyIvory
import com.example.ui.theme.AbleyTeal
import kotlinx.coroutines.delay
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.example.data.model.BreathPattern
import com.example.data.model.PlayMode
import com.example.data.model.TraceShape
import com.example.ui.play.BreathPacerGame
import com.example.ui.play.TracePathGame
import com.example.ui.play.SteadyHoldGame

@Composable
fun ActivityPlayerDialog(
    activity: MoveActivity,
    childName: String,
    onDismiss: () -> Unit,
    onCompleteActivity: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalSteps = activity.demonstrationSteps.size
    var currentStepIndex by remember { mutableIntStateOf(0) }
    var secondsRemaining by remember { mutableIntStateOf(30) }
    var isTimerRunning by remember { mutableStateOf(true) }
    var isCompleted by remember { mutableStateOf(false) }

    // Timer effect
    LaunchedEffect(isTimerRunning, secondsRemaining, currentStepIndex, isCompleted) {
        if (isTimerRunning && secondsRemaining > 0 && !isCompleted) {
            delay(1000L)
            secondsRemaining--
        } else if (secondsRemaining == 0 && !isCompleted) {
            if (currentStepIndex < totalSteps - 1) {
                currentStepIndex++
                secondsRemaining = 30
            } else {
                isCompleted = true
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            modifier = modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 24.dp)
                .testTag("activity_player_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AbleyTeal.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "${activity.categoryBadge.uppercase()} · ${activity.durationMinutes} MIN",
                            color = AbleyTeal,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_player_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = AbleyInk)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!isCompleted) {
                    Text(
                        text = activity.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = AbleyInk
                        )
                    )
                    Text(
                        text = "Step ${currentStepIndex + 1} of $totalSteps",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = AbleyInk.copy(alpha = 0.6f)
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    val haptics = LocalHapticFeedback.current

                    // Fifteen activities in the catalogue play as something other than a timer.
                    // For those the game replaces the clock face; for the rest the clock is right,
                    // because the activity is happening in the room and not on the screen.
                    when (activity.playMode) {
                        PlayMode.BREATH_PACER -> {
                            BreathPacerGame(
                                pattern = activity.breathPattern ?: BreathPattern(),
                                onComplete = {
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isCompleted = true
                                }
                            )
                        }
                        PlayMode.TRACE_PATH -> {
                            TracePathGame(
                                shape = activity.traceShape ?: TraceShape.SQUARE,
                                onComplete = {
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isCompleted = true
                                }
                            )
                        }
                        PlayMode.STEADY_HOLD -> {
                            SteadyHoldGame(
                                holdSeconds = activity.holdSeconds.coerceAtLeast(10),
                                onComplete = {
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isCompleted = true
                                }
                            )
                        }
                        PlayMode.GUIDED_STEPS -> GuidedStepsTimer(
                            secondsRemaining = secondsRemaining,
                            isTimerRunning = isTimerRunning
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Current step instructions card
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = AbleyIvory),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = activity.demonstrationSteps.getOrElse(currentStepIndex) { "Follow the rhythm and keep your balance!" },
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = AbleyInk,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 22.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Player Controls (Play / Pause, Next Step)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Play / Pause Toggle
                        IconButton(
                            onClick = { isTimerRunning = !isTimerRunning },
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(AbleyCoral)
                                .testTag("toggle_play_pause")
                        ) {
                            Icon(
                                imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isTimerRunning) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.size(16.dp))

                        // Next step button
                        IconButton(
                            onClick = {
                                if (currentStepIndex < totalSteps - 1) {
                                    currentStepIndex++
                                    secondsRemaining = 30
                                } else {
                                    isCompleted = true
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(AbleyTeal.copy(alpha = 0.15f))
                                .testTag("next_step_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipNext,
                                contentDescription = "Next Step",
                                tint = AbleyTeal,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                } else {
                    // Completion State (Key Flow Page 24)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(AbleyTeal.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = AbleyTeal,
                                modifier = Modifier.size(44.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Movement Complete!",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AbleyInk
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "+${activity.xpReward} XP Earned · ${activity.durationMinutes} Minutes Active",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = AbleyCoral,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Milestone automatically logged to $childName's My Story timeline.\nReady for your family share card!",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = AbleyInk.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = onCompleteActivity,
                            colors = ButtonDefaults.buttonColors(containerColor = AbleyCoral),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("complete_activity_and_share_button")
                        ) {
                            Text(
                                text = "View Share Card & Finish",
                                color = Color.White,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}

/** The original clock face, kept for the eighty-three activities that happen off the screen. */
@Composable
private fun GuidedStepsTimer(
    secondsRemaining: Int,
    isTimerRunning: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(160.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { secondsRemaining / 30f },
            color = AbleyCoral,
            trackColor = AbleyIvory,
            strokeWidth = 10.dp,
            modifier = Modifier.fillMaxSize()
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "00:${if (secondsRemaining < 10) "0" else ""}$secondsRemaining",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = AbleyInk,
                    fontSize = 32.sp
                )
            )
            Text(
                text = if (isTimerRunning) "PACING" else "PAUSED",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isTimerRunning) AbleyCoral else AbleyInk.copy(alpha = 0.5f),
                    letterSpacing = 1.sp
                )
            )
        }
    }
}
