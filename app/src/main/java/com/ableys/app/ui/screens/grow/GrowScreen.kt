package com.ableys.app.ui.screens.grow

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ableys.app.data.model.ChildDevelopmentMilestone
import com.ableys.app.data.model.ChildProfile
import com.ableys.app.data.model.MilestoneCategory
import com.ableys.app.data.model.MilestoneProgressStatus
import com.ableys.app.data.model.SkillArea
import com.ableys.app.data.model.SkillProgress
import com.ableys.app.ui.theme.AbleyBorder
import com.ableys.app.ui.theme.AbleyCoral
import com.ableys.app.ui.theme.AbleyCoralLight
import com.ableys.app.ui.theme.AbleyGold
import com.ableys.app.ui.theme.AbleyGoldLight
import com.ableys.app.ui.theme.AbleyInk
import com.ableys.app.ui.theme.AbleyIvory
import com.ableys.app.ui.theme.AbleySand
import com.ableys.app.ui.theme.AbleyTeal
import com.ableys.app.ui.theme.AbleyTealLight
import com.ableys.app.ui.theme.DmSansFontFamily
import com.ableys.app.ui.theme.PoppinsFontFamily

data class SkillPillarMetadata(
    val index: String,
    val focusDescription: String,
    val accentColor: Color,
    val baseTargetXp: Int
)

fun getSkillPillarMetadata(skillArea: SkillArea): SkillPillarMetadata {
    return when (skillArea) {
        SkillArea.PLAYING_WITH_OTHERS -> SkillPillarMetadata(
            index = "07",
            focusDescription = "Turn-taking, shared play and joining in with others",
            accentColor = AbleyCoral,
            baseTargetXp = 350
        )
        SkillArea.HANDS_FINE_MOTOR -> SkillPillarMetadata(
            index = "05",
            focusDescription = "Pinch and grip strength, scissors and early handwriting",
            accentColor = AbleyTeal,
            baseTargetXp = 300
        )
        SkillArea.MOVEMENT_ENERGY -> SkillPillarMetadata(
            index = "03",
            focusDescription = "Balance, coordination and burning off restless energy",
            accentColor = AbleyGold,
            baseTargetXp = 400
        )
        SkillArea.FOCUS_ATTENTION -> SkillPillarMetadata(
            index = "02",
            focusDescription = "Looking carefully, remembering, sequencing and staying with a task",
            accentColor = Color(0xFF3868A8),
            baseTargetXp = 200
        )
        SkillArea.CALM_COMFORT -> SkillPillarMetadata(
            index = "01",
            focusDescription = "Settling the body, easing big feelings and finding calm together",
            accentColor = Color(0xFFC74B6A),
            baseTargetXp = 300
        )
        SkillArea.STRENGTH_BODY_AWARENESS -> SkillPillarMetadata(
            index = "04",
            focusDescription = "Core strength, heavy work and knowing where the body is",
            accentColor = Color(0xFF2E8B75),
            baseTargetXp = 350
        )
        SkillArea.EVERYDAY_INDEPENDENCE -> SkillPillarMetadata(
            index = "06",
            focusDescription = "Dressing, mealtimes, hygiene and everyday routines",
            accentColor = Color(0xFFCC6628),
            baseTargetXp = 200
        )
    }
}

@Composable
fun GrowScreen(
    childProfile: ChildProfile?,
    skillProgressList: List<SkillProgress>,
    onSelectSkill: (SkillArea) -> Unit,
    onOpenAchievements: () -> Unit,
    milestonesList: List<ChildDevelopmentMilestone> = emptyList(),
    onOpenMilestones: () -> Unit = {},
    onStartSkillPractice: (SkillArea) -> Unit = onSelectSkill,
    modifier: Modifier = Modifier
) {
    val childName = childProfile?.name ?: "Aarav"
    val totalXp = childProfile?.totalXp ?: 1240
    val level = childProfile?.level ?: 7
    val streak = childProfile?.currentStreak ?: 12

    // The 7 parent-facing need areas, in order
    val orderedSkillAreas = listOf(
        SkillArea.CALM_COMFORT,
        SkillArea.FOCUS_ATTENTION,
        SkillArea.MOVEMENT_ENERGY,
        SkillArea.STRENGTH_BODY_AWARENESS,
        SkillArea.HANDS_FINE_MOTOR,
        SkillArea.EVERYDAY_INDEPENDENCE,
        SkillArea.PLAYING_WITH_OTHERS
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AbleyIvory)
            .padding(horizontal = 20.dp)
            .testTag("grow_screen"),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Screen Header & Anti-Preschool Editorial Greeting
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = "GOOD MORNING, ${childName.uppercase()}",
                    fontFamily = DmSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = AbleyTeal,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Learn & Grow",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = AbleyInk,
                    lineHeight = 38.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Gamified 7-row Skill Map · Tracking developmental XP across all key pillars",
                    fontFamily = DmSansFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = AbleyInk.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Gamified Child Progression & Level Explorer Card
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = AbleyInk),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .clickable { onOpenAchievements() }
                        .testTag("grow_xp_level_badge")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "LEVEL $level EXPLORER",
                                    fontFamily = DmSansFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.75f),
                                    letterSpacing = 1.2.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${"%,d".format(totalXp)} XP",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 30.sp,
                                    color = Color.White
                                )
                            }

                            // Daily Streak & Achievements Pill
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = AbleyCoral,
                                modifier = Modifier.clip(RoundedCornerShape(20.dp))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = "Streak",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "$streak Days Streak",
                                        fontFamily = DmSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Level Progress Meter
                        val progressFraction = 0.62f
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Progress to Level ${level + 1}",
                                    fontFamily = DmSansFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                                Text(
                                    text = "155 / 250 XP",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = AbleyCoralLight
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { progressFraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = AbleyCoral,
                                trackColor = Color.White.copy(alpha = 0.2f),
                                strokeCap = StrokeCap.Round
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Tap to view milestone achievements & badges",
                                fontFamily = DmSansFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.65f)
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // Child Development Milestones Tracker Card
        item {
            MilestonesGrowCard(
                milestones = milestonesList,
                onClick = onOpenMilestones
            )
        }

        // Parent-Focused Growth Insights Dashboard Component (D3-style velocity & category completion)
        item {
            GrowInsightsDashboardCard(
                childProfile = childProfile,
                milestones = milestonesList,
                skillProgressList = skillProgressList,
                onViewAllMilestones = onOpenMilestones,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Section Title: Gamified 7-Row Skill Map
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "GAMIFIED 7-ROW SKILL MAP",
                    fontFamily = DmSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = AbleyInk.copy(alpha = 0.5f),
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "7 Key Pillars · Chunky Fluid Motion",
                    fontFamily = DmSansFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = AbleyInk.copy(alpha = 0.5f)
                )
            }
        }

        // The 7 Gamified Skill Map Rows
        items(orderedSkillAreas, key = { it.id }) { skillArea ->
            val meta = getSkillPillarMetadata(skillArea)
            val progress = skillProgressList.firstOrNull { it.skillAreaId == skillArea.id }

            val currentLevel = progress?.currentLevel ?: when (skillArea) {
                SkillArea.PLAYING_WITH_OTHERS -> 4
                SkillArea.HANDS_FINE_MOTOR -> 3
                SkillArea.MOVEMENT_ENERGY -> 5
                SkillArea.FOCUS_ATTENTION -> 2
                SkillArea.CALM_COMFORT -> 3
                SkillArea.STRENGTH_BODY_AWARENESS -> 4
                SkillArea.EVERYDAY_INDEPENDENCE -> 2
            }

            val xpEarned = progress?.xpEarned ?: when (skillArea) {
                SkillArea.PLAYING_WITH_OTHERS -> 280
                SkillArea.HANDS_FINE_MOTOR -> 210
                SkillArea.MOVEMENT_ENERGY -> 340
                SkillArea.FOCUS_ATTENTION -> 130
                SkillArea.CALM_COMFORT -> 190
                SkillArea.STRENGTH_BODY_AWARENESS -> 270
                SkillArea.EVERYDAY_INDEPENDENCE -> 120
            }

            val gamesCompleted = progress?.gamesCompleted ?: when (skillArea) {
                SkillArea.PLAYING_WITH_OTHERS -> 14
                SkillArea.HANDS_FINE_MOTOR -> 10
                SkillArea.MOVEMENT_ENERGY -> 18
                SkillArea.FOCUS_ATTENTION -> 6
                SkillArea.CALM_COMFORT -> 9
                SkillArea.STRENGTH_BODY_AWARENESS -> 13
                SkillArea.EVERYDAY_INDEPENDENCE -> 5
            }

            SkillMapRowCard(
                skillArea = skillArea,
                metadata = meta,
                level = currentLevel,
                xpEarned = xpEarned,
                gamesCompleted = gamesCompleted,
                onCardClick = { onSelectSkill(skillArea) },
                onPracticeClick = { onStartSkillPractice(skillArea) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * High-fidelity, chunky, tactile Skill Row Card representing one of the 7 pillars.
 * Incorporates gamified XP tracking, milestone node progression, and interactive practice.
 */
@Composable
fun SkillMapRowCard(
    skillArea: SkillArea,
    metadata: SkillPillarMetadata,
    level: Int,
    xpEarned: Int,
    gamesCompleted: Int,
    onCardClick: () -> Unit,
    onPracticeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val targetXp = metadata.baseTargetXp
    val progressRatio = (xpEarned.toFloat() / targetXp.toFloat()).coerceIn(0.1f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progressRatio, label = "skill_progress")

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, AbleyBorder.copy(alpha = 0.65f), RoundedCornerShape(22.dp))
            .clickable { onCardClick() }
            .testTag("skill_row_${skillArea.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Row 1: Pillar Index + Title + Level Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    // Weighted so a long area name wraps inside the card instead of pushing the
                    // level chip off the edge. The needs-based names are far longer than the
                    // single words this row was laid out for.
                    modifier = Modifier.weight(1f)
                ) {
                    // Pillar Index Tag (e.g. "01", "02")
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = metadata.accentColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = metadata.index,
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = metadata.accentColor,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }

                    // Display Title in Poppins Bold
                    Text(
                        text = skillArea.displayName,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        lineHeight = 22.sp,
                        maxLines = 2,
                        color = AbleyInk,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }

                // Level Badge in Poppins
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = AbleyTealLight
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Level $level",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = AbleyTeal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Row 2: Short Description in DM Sans
            Text(
                text = metadata.focusDescription,
                fontFamily = DmSansFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = AbleyInk.copy(alpha = 0.72f),
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Row 3: Gamified XP Meter
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Developmental XP",
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        color = AbleyInk.copy(alpha = 0.55f)
                    )
                    Text(
                        text = "$xpEarned / $targetXp XP",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = metadata.accentColor
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = metadata.accentColor,
                    trackColor = AbleySand,
                    strokeCap = StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Row 4: Chunky Milestone Nodes Progression Path (Child Experience: Minimalist Playfulness)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = AbleySand.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MILESTONES",
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = AbleyInk.copy(alpha = 0.5f),
                        letterSpacing = 1.0.sp
                    )

                    // 5-Node Visual Progression Map
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (nodeIndex in 1..5) {
                            when {
                                nodeIndex < level -> {
                                    // Mastered Milestone Node
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(AbleyTeal),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Completed",
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }
                                nodeIndex == level -> {
                                    // Current Active Milestone Node (Calming celebration ring)
                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, AbleyCoral, CircleShape)
                                            .background(AbleyCoralLight),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$nodeIndex",
                                            fontFamily = PoppinsFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = AbleyCoral
                                        )
                                    }
                                }
                                else -> {
                                    // Upcoming Milestone Node
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(AbleyBorder)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Row 5: Action Controls & Practice Trigger
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$gamesCompleted learning sessions logged",
                    fontFamily = DmSansFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = AbleyInk.copy(alpha = 0.6f)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Practice Button
                    Button(
                        onClick = onPracticeClick,
                        colors = ButtonDefaults.buttonColors(containerColor = AbleyInk),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .height(38.dp)
                            .testTag("practice_skill_${skillArea.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = AbleyIvory,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Practice",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = AbleyIvory
                            )
                        }
                    }

                    // Roadmap Chevron
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = AbleySand.copy(alpha = 0.6f),
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .clickable { onCardClick() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "View Roadmap",
                                tint = AbleyInk,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MilestonesGrowCard(
    milestones: List<ChildDevelopmentMilestone>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalCount = milestones.size.coerceAtLeast(1)
    val achievedCount = milestones.count { it.progressStatus == MilestoneProgressStatus.ACHIEVED }
    val progressFraction = (achievedCount.toFloat() / totalCount.toFloat()).coerceIn(0f, 1f)

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, AbleyBorder.copy(alpha = 0.65f), RoundedCornerShape(22.dp))
            .clickable { onClick() }
            .testTag("grow_milestones_card")
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = AbleyTealLight,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("🌱", fontSize = 15.sp)
                        }
                    }

                    Text(
                        text = "DEVELOPMENT MILESTONES",
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = AbleyInk.copy(alpha = 0.55f),
                        letterSpacing = 1.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AbleyTealLight
                ) {
                    Text(
                        text = "$achievedCount / ${milestones.size} Mastered",
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = AbleyTeal,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Clinical 48–60 Month Milestones",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = AbleyInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Tracking gross motor, cognitive, and speech milestones verified against pediatric clinical developmental standards.",
                fontFamily = DmSansFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = AbleyInk.copy(alpha = 0.7f),
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = AbleyTeal,
                trackColor = AbleySand,
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tap to open milestone checklists",
                    fontFamily = DmSansFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = AbleyTeal
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = AbleyTeal,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
