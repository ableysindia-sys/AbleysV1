package com.example.ui.screens.support

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleyInk
import com.example.ui.theme.AbleyIvory
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal
import com.example.ui.theme.DmSansFontFamily
import com.example.ui.theme.PoppinsFontFamily
import kotlinx.coroutines.delay

/**
 * Editorial Countdown Timer Overlay for Guided Therapy Session Loops.
 * Overlaid directly on the session loop player in the Support tab, allowing parents to track
 * session duration, active phase (Warm-Up, Active Practice, Calming Reset), and control playback.
 * Strictly adheres to Abley's Anti-Preschool aesthetic and V1 Palette (Teal, Coral, Ivory, Ink).
 */
@Composable
fun TherapyCountdownTimerOverlay(
    durationMinutes: Int,
    programTitle: String,
    clinicalTarget: String,
    onClose: () -> Unit,
    onLaunchFullSession: () -> Unit,
    modifier: Modifier = Modifier,
    onSessionCompleted: () -> Unit = {}
) {
    val totalSeconds = (durationMinutes * 60).coerceAtLeast(60)
    var secondsRemaining by remember(durationMinutes) { mutableIntStateOf(totalSeconds) }
    var isRunning by remember { mutableStateOf(true) }
    var isCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning, secondsRemaining, isCompleted) {
        if (isRunning && secondsRemaining > 0 && !isCompleted) {
            delay(1000L)
            secondsRemaining--
        } else if (secondsRemaining == 0 && !isCompleted) {
            isCompleted = true
            onSessionCompleted()
        }
    }

    val progress = (secondsRemaining.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)
    val elapsedFraction = 1f - progress

    val phaseLabel = when {
        elapsedFraction < 0.25f -> "Phase 1/3 · Sensory Warm-Up"
        elapsedFraction < 0.75f -> "Phase 2/3 · Active Therapeutic Practice"
        else -> "Phase 3/3 · Calming Reset"
    }

    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val formattedTime = "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = AbleyInk.copy(alpha = 0.94f),
        modifier = modifier
            .fillMaxWidth()
            .testTag("therapy_countdown_timer_overlay")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // Top Bar: Phase Tag, Clinical Target, Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = AbleyTeal.copy(alpha = 0.9f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = AbleyIvory,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = phaseLabel.uppercase(),
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = AbleyIvory,
                            letterSpacing = 0.6.sp,
                            modifier = Modifier.testTag("therapy_timer_phase_indicator")
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.White.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "${durationMinutes}M GUIDED LOOP",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = AbleyIvory.copy(alpha = 0.8f),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(26.dp)
                            .testTag("therapy_timer_close")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Overlay",
                            tint = AbleyIvory.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Center: Big High-Contrast Countdown Clock + Target
            if (!isCompleted) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = formattedTime,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = AbleyIvory,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.testTag("therapy_timer_countdown_text")
                        )
                        Text(
                            text = if (isRunning) "SESSION IN PROGRESS · $clinicalTarget" else "PAUSED · CHECK CHILD COMFORT",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp,
                            color = if (isRunning) AbleyIvory.copy(alpha = 0.65f) else AbleyCoral
                        )
                    }

                    // Progress Pill Bar
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AbleyTeal.copy(alpha = 0.15f),
                        modifier = Modifier.border(1.dp, AbleyTeal.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    ) {
                        Text(
                            text = "${(progress * 100).toInt()}% REMAINING",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = AbleyTeal,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Smooth Progress Track
                LinearProgressIndicator(
                    progress = { progress },
                    color = AbleyTeal,
                    trackColor = AbleyIvory.copy(alpha = 0.15f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Interactive Controls Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Play / Pause Button
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (isRunning) AbleyTeal else AbleyCoral)
                                .clickable { isRunning = !isRunning }
                                .testTag("therapy_timer_play_pause"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isRunning) "Pause" else "Play",
                                tint = AbleyIvory,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Reset Button
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.12f))
                                .clickable {
                                    secondsRemaining = totalSeconds
                                    isRunning = false
                                }
                                .testTag("therapy_timer_reset"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset Timer",
                                tint = AbleyIvory.copy(alpha = 0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // +1 Min Quick Adjuster
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White.copy(alpha = 0.12f),
                            modifier = Modifier
                                .clickable { secondsRemaining = (secondsRemaining + 60).coerceAtMost(totalSeconds + 300) }
                                .testTag("therapy_timer_add_minute")
                        ) {
                            Text(
                                text = "+1 MIN",
                                fontFamily = DmSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = AbleyIvory,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }

                    // Full Guided Player CTA
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = AbleySand.copy(alpha = 0.15f),
                        modifier = Modifier
                            .clickable { onLaunchFullSession() }
                            .testTag("therapy_timer_open_full")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Full OT Mode",
                                fontFamily = DmSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = AbleyIvory
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                contentDescription = null,
                                tint = AbleyIvory,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            } else {
                // Completed Session State
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = AbleyTeal,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Guided Therapy Loop Complete!",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = AbleyIvory
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Completed $durationMinutes minutes of intentional regulation · Zero pressure.",
                        fontFamily = DmSansFontFamily,
                        fontSize = 11.sp,
                        color = AbleyIvory.copy(alpha = 0.75f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                secondsRemaining = totalSeconds
                                isCompleted = false
                                isRunning = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AbleyTeal),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("Repeat Loop", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onClose,
                            colors = ButtonDefaults.buttonColors(containerColor = AbleySand.copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("Dismiss", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AbleyIvory)
                        }
                    }
                }
            }
        }
    }
}
