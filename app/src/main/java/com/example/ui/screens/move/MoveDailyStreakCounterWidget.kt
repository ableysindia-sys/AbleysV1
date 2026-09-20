package com.example.ui.screens.move

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleyCoralLight
import com.example.ui.theme.AbleyInk
import com.example.ui.theme.AbleyIvory
import com.example.ui.theme.AbleySand
import com.example.ui.theme.DmSansFontFamily
import com.example.ui.theme.PoppinsFontFamily

/**
 * Daily Streak Counter Widget for the 'Move' Tab
 * Tracks consecutive days of activity utilizing the high-contrast Coral (#EE4A41) palette.
 * Designed with Nike Training Club / Apple Fitness+ inspired editorial typography.
 */
@Composable
fun MoveDailyStreakCounterWidget(
    currentStreakDays: Int,
    nextMilestoneDays: Int = 14,
    modifier: Modifier = Modifier,
    onStreakClick: () -> Unit = {}
) {
    var isCelebrated by remember { mutableStateOf(false) }

    // Weekday indicators data (Mon - Sun)
    // For a 12-day streak, past days are active, today is active
    val daysOfWeek = remember {
        listOf(
            DayStreakInfo("M", "Mon", isCompleted = true, isToday = false),
            DayStreakInfo("T", "Tue", isCompleted = true, isToday = false),
            DayStreakInfo("W", "Wed", isCompleted = true, isToday = false),
            DayStreakInfo("T", "Thu", isCompleted = true, isToday = false),
            DayStreakInfo("F", "Fri", isCompleted = true, isToday = false),
            DayStreakInfo("S", "Sat", isCompleted = true, isToday = true), // Current active day
            DayStreakInfo("S", "Sun", isCompleted = false, isToday = false) // Upcoming
        )
    }

    val progressToMilestone = (currentStreakDays.toFloat() / nextMilestoneDays.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progressToMilestone,
        animationSpec = tween(durationMillis = 800),
        label = "streak_progress"
    )

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, AbleyCoral.copy(alpha = 0.25f)),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .testTag("move_daily_streak_widget")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header: Coral badge + motivation tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AbleyCoralLight
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak Fire",
                            tint = AbleyCoral,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "DAILY STREAK COUNTER",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = AbleyCoral,
                            letterSpacing = 1.2.sp
                        )
                    }
                }

                // Active status pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AbleyCoral
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Text(
                            text = "ACTIVE",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color.White,
                            letterSpacing = 0.8.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Streak Counter Numbers in Poppins (High Contrast Coral)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "$currentStreakDays",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 42.sp,
                            color = AbleyCoral,
                            lineHeight = 44.sp,
                            modifier = Modifier.testTag("streak_days_count")
                        )
                        Text(
                            text = "DAYS IN A ROW",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = AbleyInk,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Consecutive physical play & shared movement",
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = AbleyInk.copy(alpha = 0.7f)
                    )
                }

                // Coral Flame Accent Circle
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(AbleyCoral, Color(0xFFFF6B60))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Active Habit Flame",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 7-Day Visual Week Streak Tracker (High-contrast Coral day markers)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(AbleySand.copy(alpha = 0.45f))
                    .padding(vertical = 12.dp, horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                daysOfWeek.forEachIndexed { index, day ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .testTag("streak_day_indicator_$index")
                            .weight(1f)
                    ) {
                        Text(
                            text = day.label,
                            fontFamily = DmSansFontFamily,
                            fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp,
                            color = if (day.isToday) AbleyCoral else AbleyInk.copy(alpha = 0.6f)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        day.isCompleted -> AbleyCoral
                                        day.isToday -> AbleyCoralLight
                                        else -> Color.White
                                    }
                                )
                                .then(
                                    if (day.isToday && !day.isCompleted) {
                                        Modifier.border(2.dp, AbleyCoral, CircleShape)
                                    } else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (day.isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Active day",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else if (day.isToday) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(AbleyCoral)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(AbleyInk.copy(alpha = 0.25f))
                                )
                            }
                        }

                        if (day.isToday) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "TODAY",
                                fontFamily = DmSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 8.sp,
                                color = AbleyCoral,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Milestone Target Bar (Coral Palette Progress)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("streak_milestone_progress")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = null,
                            tint = AbleyCoral,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Next Milestone: $nextMilestoneDays Days",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = AbleyInk
                        )
                    }

                    val daysRemaining = (nextMilestoneDays - currentStreakDays).coerceAtLeast(0)
                    Text(
                        text = if (daysRemaining > 0) "$daysRemaining days to go" else "Milestone Reached! 🎉",
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = AbleyCoral
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = AbleyCoral,
                    trackColor = AbleyCoralLight,
                    strokeCap = StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom micro-copy: Motivational intentional parenting statement
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.OfflineBolt,
                    contentDescription = null,
                    tint = AbleyCoral,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "Intentional Habit · Real room movement builds lifelong sensory regulation",
                    fontFamily = DmSansFontFamily,
                    fontSize = 11.sp,
                    color = AbleyInk.copy(alpha = 0.65f)
                )
            }
        }
    }
}

private data class DayStreakInfo(
    val letter: String,
    val label: String,
    val isCompleted: Boolean,
    val isToday: Boolean
)
