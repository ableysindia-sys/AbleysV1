package com.ableys.app.ui.screens.grow

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ableys.app.data.model.SkillArea
import com.ableys.app.data.model.SkillProgress
import com.ableys.app.ui.theme.AbleyCoral
import com.ableys.app.ui.theme.AbleyCoralLight
import com.ableys.app.ui.theme.AbleyGold
import com.ableys.app.ui.theme.AbleyInk
import com.ableys.app.ui.theme.AbleyIvory
import com.ableys.app.ui.theme.AbleySand
import com.ableys.app.ui.theme.AbleySandLight
import com.ableys.app.ui.theme.AbleyTeal

@Composable
fun SkillDetailScreen(
    skillArea: SkillArea,
    progress: SkillProgress?,
    onBack: () -> Unit,
    onStartGame: () -> Unit,
    onOpenShopItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentLevel = progress?.currentLevel ?: 1
    val xp = progress?.xpEarned ?: 100

    Scaffold(
        containerColor = AbleyIvory,
        topBar = {
            Surface(
                color = AbleyIvory,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AbleyInk
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = skillArea.displayName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AbleyInk
                            )
                        )
                        Text(
                            text = "Level $currentLevel · $xp XP Earned",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = AbleyInk.copy(alpha = 0.6f)
                            )
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = AbleyCoralLight
                    ) {
                        Text(
                            text = "Lv $currentLevel",
                            color = AbleyCoral,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        },
        modifier = modifier.testTag("skill_detail_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Progression Loop Banner (Page 9)
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "THE PROGRESSION LOOP",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = AbleyCoral,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Area → Sessions → Streak → Consistency",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = AbleyInk
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = skillArea.shortDescription,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = AbleyInk.copy(alpha = 0.7f)
                            )
                        )
                    }
                }
            }

            // Primary Call to Action
            item {
                Button(
                    onClick = onStartGame,
                    colors = ButtonDefaults.buttonColors(containerColor = AbleyCoral),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("play_game_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Play Level $currentLevel Game (+25 XP)",
                        color = Color.White,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            // Level Roadmap (1 to 8)
            item {
                Text(
                    text = "Developmental Roadmap",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = AbleyInk
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(8) { levelIdx ->
                val levelNum = levelIdx + 1
                val isMastered = levelNum < currentLevel
                val isCurrent = levelNum == currentLevel
                val isLocked = levelNum > currentLevel

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCurrent) Color.White else if (isMastered) AbleySand.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.4f)
                    ),
                    border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.5.dp, AbleyCoral) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = isCurrent) { onStartGame() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isMastered -> AbleyTeal
                                            isCurrent -> AbleyCoral
                                            else -> AbleySand
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                when {
                                    isMastered -> Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                                    isCurrent -> Text("$levelNum", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    else -> Icon(Icons.Default.Lock, contentDescription = null, tint = AbleyInk.copy(alpha = 0.4f), modifier = Modifier.size(18.dp))
                                }
                            }

                            Column {
                                Text(
                                    text = "Stage $levelNum: ${getLevelTitle(skillArea, levelNum)}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isLocked) AbleyInk.copy(alpha = 0.4f) else AbleyInk
                                    )
                                )
                                Text(
                                    text = if (isMastered) "Mastered · Goal Achieved" else if (isCurrent) "Ready to play · Tap to start" else "Unlocks at Level $levelNum",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (isCurrent) AbleyCoral else AbleyInk.copy(alpha = 0.5f),
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        if (isCurrent) {
                            Text(
                                text = "PLAY",
                                color = AbleyCoral,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Contextual Shop Card (Page 16)
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AbleySandLight),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AbleyCoral.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "CONTINUE OFFLINE · PHYSICAL PLAY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = AbleyCoral,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Play Pattern Board",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = AbleyInk
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Screen play hands off to a physical tactile toy. Supports fine motor and bilateral coordination.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = AbleyInk.copy(alpha = 0.7f),
                                lineHeight = 18.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { onOpenShopItem("SKU-BOARD-03") },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AbleyCoral),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.testTag("open_pattern_board_shop")
                        ) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.size(6.dp))
                            Text("Open Play Pattern Board", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private fun getLevelTitle(area: SkillArea, level: Int): String {
    return when (area) {
        SkillArea.PLAYING_WITH_OTHERS -> listOf("One-Prop Switch", "Pretend Play Ladder", "Shared Plane", "Three-Step Request", "Simon's Classroom", "Morning Meeting", "Tool Play", "Joining In").getOrElse(level - 1) { "Keeping It Up" }
        SkillArea.HANDS_FINE_MOTOR -> listOf("Squeeze and Release", "Grasp Check", "Snip the Curve", "Zigzag Trails", "Line Laps", "Top-Down Tracing", "Message Making", "Grip, Carry, Place").getOrElse(level - 1) { "Keeping It Up" }
        SkillArea.MOVEMENT_ENERGY -> listOf("Stepping Stones", "Quiet Feet", "Bear Crawl Relay", "Step Over, Step In", "Throw and Catch", "Push and Glide", "Core Up and Reach", "Moving Together").getOrElse(level - 1) { "Keeping It Up" }
        SkillArea.FOCUS_ATTENTION -> listOf("Treasure Hunt", "Memory Snapshots", "Hidden Paths", "Watch and Respond", "Sequencing Slides", "Switchboard", "Try, Check, Adjust", "Steady Focus").getOrElse(level - 1) { "Keeping It Up" }
        SkillArea.CALM_COMFORT -> listOf("Quiet Corner", "Slow Exhale", "Deep Pressure", "Calm Carry", "Heavy Work Reset", "Transition Bridge", "Sensory Check-in", "Settling Together").getOrElse(level - 1) { "Keeping It Up" }
        SkillArea.STRENGTH_BODY_AWARENESS -> listOf("Wall Push", "Bear Drag", "Inchworm Ladder", "Weighted Carry", "Scooter Glide", "Heavy Work Circuit", "Body Boundaries", "Strong and Steady").getOrElse(level - 1) { "Keeping It Up" }
        SkillArea.EVERYDAY_INDEPENDENCE -> listOf("Hand Washing", "Shoe & Socks Routine", "Teeth Brushing", "Morning Prep Strip", "Tidying Play Area", "Table Manners", "Safety Awareness", "Independent Star").getOrElse(level - 1) { "Keeping It Up" }
    }
}
