package com.example.ui.screens.support

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
import androidx.compose.material.icons.filled.TipsAndUpdates
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
import com.example.data.model.TherapyProgram
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleyInk
import com.example.ui.theme.AbleyIvory
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal
import kotlinx.coroutines.delay

@Composable
fun TherapySessionPlayerDialog(
    program: TherapyProgram,
    childName: String,
    onDismiss: () -> Unit,
    onCompleteSession: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalSteps = program.steps.size
    var currentStepIndex by remember { mutableIntStateOf(0) }
    var secondsRemaining by remember { mutableIntStateOf(45) }
    var isTimerRunning by remember { mutableStateOf(true) }
    var isCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(isTimerRunning, secondsRemaining, currentStepIndex, isCompleted) {
        if (isTimerRunning && secondsRemaining > 0 && !isCompleted) {
            delay(1000L)
            secondsRemaining--
        } else if (secondsRemaining == 0 && !isCompleted) {
            if (currentStepIndex < totalSteps - 1) {
                currentStepIndex++
                secondsRemaining = 45
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
                .testTag("therapy_session_player_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
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
                            text = "${program.area.displayName.uppercase()} · OT/PT GUIDED",
                            color = AbleyTeal,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_therapy_player")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = AbleyInk)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!isCompleted) {
                    Text(
                        text = program.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = AbleyInk
                        )
                    )
                    Text(
                        text = "Step ${currentStepIndex + 1} of $totalSteps · Gentle Pacing",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = AbleyInk.copy(alpha = 0.6f)
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Timer Circular Graphic
                    Box(
                        modifier = Modifier.size(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { secondsRemaining / 45f },
                            color = AbleyTeal,
                            trackColor = AbleyIvory,
                            strokeWidth = 9.dp,
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
                                text = if (isTimerRunning) "PRACTICE" else "PAUSED",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isTimerRunning) AbleyTeal else AbleyInk.copy(alpha = 0.5f),
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Step Guidance Card
                    val activeStep = program.steps.getOrNull(currentStepIndex)
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = AbleyIvory),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = activeStep?.name ?: "Step ${currentStepIndex + 1}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AbleyInk
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = activeStep?.instruction ?: "Maintain steady cadence and check child comfort.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = AbleyInk.copy(alpha = 0.85f),
                                    lineHeight = 22.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // OT Parent Clinical Tip
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = AbleySand.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.TipsAndUpdates,
                                contentDescription = null,
                                tint = AbleyCoral,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = program.parentTips.getOrElse(currentStepIndex % program.parentTips.size) { "Keep eyes on child's breathing and autonomic responses." },
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = AbleyInk.copy(alpha = 0.85f),
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { isTimerRunning = !isTimerRunning },
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(AbleyTeal)
                        ) {
                            Icon(
                                imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isTimerRunning) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.size(16.dp))

                        IconButton(
                            onClick = {
                                if (currentStepIndex < totalSteps - 1) {
                                    currentStepIndex++
                                    secondsRemaining = 45
                                } else {
                                    isCompleted = true
                                }
                            },
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(AbleySand)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipNext,
                                contentDescription = "Next Step",
                                tint = AbleyInk,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                } else {
                    // Session Completed
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
                            text = "Therapy Session Completed",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AbleyInk
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "+${program.xpReward} XP · ${program.durationMinutes} Minutes",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = AbleyCoral,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Logged to $childName's development timeline.\nConsistency at home creates true long-term motor regulation.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = AbleyInk.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = onCompleteSession,
                            colors = ButtonDefaults.buttonColors(containerColor = AbleyTeal),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("finish_therapy_session_button")
                        ) {
                            Text(
                                text = "Save & Finish Session",
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
