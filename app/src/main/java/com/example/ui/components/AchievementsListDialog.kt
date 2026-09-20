package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.model.Achievement
import com.example.data.model.ShareCardData
import com.example.data.model.ShareCardTheme
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleyCoralLight
import com.example.ui.theme.AbleyInk
import com.example.ui.theme.AbleySurfaceDark
import com.example.ui.theme.AbleySurfaceDarkCard

@Composable
fun AchievementsListDialog(
    achievements: List<Achievement>,
    childName: String,
    onDismiss: () -> Unit,
    onShareAchievement: (ShareCardData) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF191615), // Elegant dark slate canvas matching Page 19
            modifier = modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 20.dp)
                .testTag("achievements_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ACHIEVEMENTS",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = AbleyCoral,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Elegant Distinctions",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Not cartoon trophies · Taste + pride",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_achievements_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(achievements, key = { it.code }) { achievement ->
                        AchievementGridCard(
                            achievement = achievement,
                            onShareClick = {
                                onShareAchievement(
                                    ShareCardData(
                                        title = achievement.title.uppercase(),
                                        bigNumber = achievement.badgeSymbol,
                                        unitLabel = "MILESTONE ACHIEVED",
                                        statsSubtitle = achievement.description,
                                        childName = childName,
                                        ageOrYear = achievement.unlockedDate ?: "2026",
                                        theme = ShareCardTheme.DARK_STRAVA,
                                        footerMessage = "KEEP GROWING →"
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AchievementGridCard(
    achievement: Achievement,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = AbleySurfaceDarkCard),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(enabled = achievement.isUnlocked) { onShareClick() }
            .testTag("achievement_card_${achievement.code}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Elegant Distinction Circular Badge
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.5.dp,
                        color = if (achievement.isUnlocked) AbleyCoral else Color.White.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .background(if (achievement.isUnlocked) AbleyCoral.copy(alpha = 0.15f) else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = achievement.badgeSymbol,
                    color = if (achievement.isUnlocked) AbleyCoral else Color.White.copy(alpha = 0.4f),
                    fontWeight = FontWeight.Bold,
                    fontSize = if (achievement.badgeSymbol.length > 2) 16.sp else 22.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = achievement.title,
                color = if (achievement.isUnlocked) Color.White else Color.White.copy(alpha = 0.6f),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (achievement.isUnlocked) (achievement.unlockedDate ?: "Earned") else "${achievement.progress} / ${achievement.maxProgress}",
                color = if (achievement.isUnlocked) AbleyCoral else Color.White.copy(alpha = 0.4f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            if (!achievement.isUnlocked) {
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { achievement.progress.toFloat() / achievement.maxProgress.toFloat() },
                    color = AbleyCoral,
                    trackColor = Color.White.copy(alpha = 0.1f),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                )
            } else {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Share card",
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
