package com.ableys.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.ableys.app.data.model.ShareCardData
import com.ableys.app.data.model.ShareCardTheme
import com.ableys.app.ui.theme.AbleyCoral

data class YearStoryBeat(
    val title: String,
    val statNumber: String,
    val statLabel: String,
    val narrative: String,
    val emoji: String
)

@Composable
fun YearInGrowingDialog(
    childName: String,
    onDismiss: () -> Unit,
    onShareSummary: (ShareCardData) -> Unit,
    modifier: Modifier = Modifier
) {
    val beats = remember(childName) {
        listOf(
            YearStoryBeat(
                title = "Abley's Year in Growing",
                statNumber = "2027",
                statLabel = "$childName's Year",
                narrative = "In the spirit of showing up together every single day.",
                emoji = "🌱"
            ),
            YearStoryBeat(
                title = "CONSISTENCY",
                statNumber = "184",
                statLabel = "active days together",
                narrative = "Over half the year dedicated to intentional shared moments.",
                emoji = "☀️"
            ),
            YearStoryBeat(
                title = "MOVEMENT",
                statNumber = "1,240",
                statLabel = "minutes moving together",
                narrative = "Balancing, leaping, tumbling, and laughing across the living room.",
                emoji = "🏃"
            ),
            YearStoryBeat(
                title = "MASTERY",
                statNumber = "137",
                statLabel = "skills mastered",
                narrative = "From calm corners and steady hands to balance steps taken together.",
                emoji = "⭐"
            ),
            YearStoryBeat(
                title = "EXPLORATION",
                statNumber = "42",
                statLabel = "new experiences",
                narrative = "Bicycle rides, stepping stone rivers, and sensory calm.",
                emoji = "🧭"
            ),
            YearStoryBeat(
                title = "RETENTION",
                statNumber = "286",
                statLabel = "memories captured",
                narrative = "We captured one little piece of childhood every day.",
                emoji = "📸"
            ),
            YearStoryBeat(
                title = "GROWTH MILESTONE",
                statNumber = "12",
                statLabel = "developmental milestones",
                narrative = "Celebrate the quiet transformations that mean everything.",
                emoji = "🏅"
            ),
            YearStoryBeat(
                title = "THE PROMISE",
                statNumber = "We",
                statLabel = "showed up.",
                narrative = "Taste · Achievement · Identity · Pride.\nKeep growing together forever.",
                emoji = "❤️"
            )
        )
    }

    var currentBeatIndex by remember { mutableIntStateOf(0) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            color = Color(0xFF141211), // Elegant dark slate
            modifier = modifier
                .fillMaxSize()
                .testTag("year_in_growing_fullscreen")
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    // Segmented Story Bars at top
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        beats.indices.forEach { index ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(
                                        when {
                                            index < currentBeatIndex -> AbleyCoral
                                            index == currentBeatIndex -> AbleyCoral
                                            else -> Color.White.copy(alpha = 0.2f)
                                        }
                                    )
                            )
                        }
                    }

                    // Top Bar: Brand + Close
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "abley's · YEAR IN GROWING",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = AbleyCoral,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("close_wrapped_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(0.15f))

                    // Animated Beat Content
                    val beat = beats[currentBeatIndex]
                    AnimatedContent(
                        targetState = beat,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        label = "beat_transition"
                    ) { targetBeat ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = targetBeat.emoji,
                                fontSize = 48.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = targetBeat.title,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = AbleyCoral,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.2.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = targetBeat.statNumber,
                                style = MaterialTheme.typography.displayLarge.copy(
                                    color = Color.White,
                                    fontSize = if (targetBeat.statNumber == "We") 64.sp else 72.sp,
                                    lineHeight = 76.sp,
                                    fontWeight = FontWeight.Black
                                )
                            )

                            Text(
                                text = targetBeat.statLabel,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = targetBeat.narrative,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.White.copy(alpha = 0.7f),
                                    lineHeight = 26.sp
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(0.15f))

                    // Bottom Navigation or Final Share Action
                    if (currentBeatIndex == beats.lastIndex) {
                        Button(
                            onClick = {
                                onShareSummary(
                                    ShareCardData(
                                        title = "YEAR IN GROWING",
                                        bigNumber = "1,240",
                                        unitLabel = "MINUTES TOGETHER",
                                        statsSubtitle = "184 active days · 137 skills mastered · 286 memories · 12 milestones\nWe showed up.",
                                        childName = childName,
                                        ageOrYear = "2027",
                                        theme = ShareCardTheme.DARK_STRAVA,
                                        footerMessage = "KEEP GROWING →"
                                    )
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AbleyCoral),
                            shape = RoundedCornerShape(28.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("share_year_in_growing_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = "Share $childName's Year in Growing",
                                color = Color.White,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Tap anywhere to continue",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.4f)
                                )
                            )

                            Button(
                                onClick = {
                                    if (currentBeatIndex < beats.lastIndex) {
                                        currentBeatIndex++
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AbleyCoral.copy(alpha = 0.2f)),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.testTag("next_story_beat_button")
                            ) {
                                Text(
                                    text = "Next →",
                                    color = AbleyCoral,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Invisible touch zones for tap next / tap previous
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (currentBeatIndex > 0) currentBeatIndex--
                            }
                    )
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (currentBeatIndex < beats.lastIndex) currentBeatIndex++
                            }
                    )
                }
            }
        }
    }
}
