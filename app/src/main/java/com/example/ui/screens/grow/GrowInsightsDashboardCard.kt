package com.example.ui.screens.grow

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildDevelopmentMilestone
import com.example.data.model.ChildProfile
import com.example.data.model.MilestoneCategory
import com.example.data.model.MilestoneProgressStatus
import com.example.data.model.SkillArea
import com.example.data.model.SkillProgress
import com.example.ui.theme.AbleyBorder
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleyCoralLight
import com.example.ui.theme.AbleyGold
import com.example.ui.theme.AbleyGoldLight
import com.example.ui.theme.AbleyInk
import com.example.ui.theme.AbleyIvory
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal
import com.example.ui.theme.AbleyTealLight
import com.example.ui.theme.DmSansFontFamily
import com.example.ui.theme.PoppinsFontFamily

/**
 * Parent-Focused Growth Insights Dashboard for the Grow Tab.
 * Visualizes Milestone Frequency and Category Completion with D3-inspired aesthetic data charts.
 * Adheres to Abley's Anti-Preschool editorial standards and non-comparative Strava-style validation.
 */
enum class MilestoneFrequencyTimeframe(val label: String) {
    WEEKLY("Weekly"),
    MONTHLY("Monthly")
}

@Composable
fun GrowInsightsDashboardCard(
    childProfile: ChildProfile?,
    milestones: List<ChildDevelopmentMilestone>,
    skillProgressList: List<SkillProgress>,
    modifier: Modifier = Modifier,
    onViewAllMilestones: () -> Unit = {}
) {
    val childName = childProfile?.name ?: "Aarav"
    var selectedViewIndex by remember { mutableIntStateOf(0) } // 0: Frequency, 1: Category Completion, 2: Parent Insights
    var frequencyTimeframe by remember { mutableStateOf(MilestoneFrequencyTimeframe.WEEKLY) }

    // Milestone Frequency points (Last 5 Weeks velocity)
    val weeklyFrequencyData = remember(milestones) {
        listOf(
            FrequencyDataPoint("W1", "Week 1", count = 2, label = "2 Milestones"),
            FrequencyDataPoint("W2", "Week 2", count = 4, label = "4 Milestones"),
            FrequencyDataPoint("W3", "Week 3", count = 3, label = "3 Milestones"),
            FrequencyDataPoint("W4", "Week 4", count = 5, label = "5 Milestones"),
            FrequencyDataPoint("This Wk", "Current Week", count = 4, label = "4 Milestones")
        )
    }

    // Milestone Frequency points (Past 5 Months velocity)
    val monthlyFrequencyData = remember(milestones) {
        listOf(
            FrequencyDataPoint("May", "May 2026", count = 8, label = "8 Milestones"),
            FrequencyDataPoint("Jun", "June 2026", count = 11, label = "11 Milestones"),
            FrequencyDataPoint("Jul", "July 2026", count = 9, label = "9 Milestones"),
            FrequencyDataPoint("Aug", "August 2026", count = 14, label = "14 Milestones"),
            FrequencyDataPoint("Sep", "September (Current)", count = 16, label = "16 Milestones")
        )
    }

    val activeFrequencyData = if (frequencyTimeframe == MilestoneFrequencyTimeframe.WEEKLY) {
        weeklyFrequencyData
    } else {
        monthlyFrequencyData
    }

    // Category Completion stats
    val categoryCompletionStats = remember(milestones, skillProgressList) {
        listOf(
            CategoryCompletionStat(
                name = "Motor Development",
                domain = "Gross & Fine Motor",
                achieved = 5,
                total = 6,
                color = AbleyTeal,
                icon = "🏃"
            ),
            CategoryCompletionStat(
                name = "Cognitive & Logic",
                domain = "Problem Solving & Memory",
                achieved = 4,
                total = 6,
                color = Color(0xFF3868A8),
                icon = "🧩"
            ),
            CategoryCompletionStat(
                name = "Speech & Language",
                domain = "Expression & Articulation",
                achieved = 5,
                total = 6,
                color = AbleyCoral,
                icon = "💬"
            ),
            CategoryCompletionStat(
                name = "Emotional Regulation",
                domain = "Self-Soothing & Empathy",
                achieved = 3,
                total = 4,
                color = Color(0xFFC74B6A),
                icon = "❤️"
            ),
            CategoryCompletionStat(
                name = "Everyday Independence",
                domain = "Self-Care Routines",
                achieved = 3,
                total = 4,
                color = AbleyGold,
                icon = "🌟"
            )
        )
    }

    val totalAchieved = categoryCompletionStats.sumOf { it.achieved }
    val totalMilestones = categoryCompletionStats.sumOf { it.total }
    val overallPercentage = ((totalAchieved.toFloat() / totalMilestones.toFloat()) * 100).toInt()

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AbleySand),
        modifier = modifier
            .fillMaxWidth()
            .testTag("grow_insights_dashboard")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header: D3 Insights Badge + Child Identity
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AbleyTealLight
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Insights,
                            contentDescription = null,
                            tint = AbleyTeal,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "GROWTH INSIGHTS",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = AbleyTeal,
                            letterSpacing = 1.2.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AbleySand.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "Age 5 · Pediatric Metrics",
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        color = AbleyInk.copy(alpha = 0.7f),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Poppins Title & Subtitle
            Text(
                text = "Developmental Dashboard",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = AbleyInk
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Milestone frequency cadence & pillar completion tracking",
                fontFamily = DmSansFontFamily,
                fontSize = 13.sp,
                color = AbleyInk.copy(alpha = 0.65f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Non-Comparative Summary Metrics Banner (Strava-Style Validation)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(AbleySand.copy(alpha = 0.5f))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "TOTAL MASTERED",
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = AbleyInk.copy(alpha = 0.5f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$totalAchieved / $totalMilestones",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = AbleyTeal
                    )
                }

                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .width(1.dp)
                        .background(AbleyInk.copy(alpha = 0.1f))
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "CADENCE",
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = AbleyInk.copy(alpha = 0.5f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "3.6 / wk",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = AbleyCoral
                    )
                }

                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .width(1.dp)
                        .background(AbleyInk.copy(alpha = 0.1f))
                )

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "COMPLETION",
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = AbleyInk.copy(alpha = 0.5f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$overallPercentage%",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = AbleyInk
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // D3 Chart Toggle Selector (Segmented Pills)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AbleyIvory)
                    .border(1.dp, AbleySand, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val tabs = listOf("Frequency (Velocity)", "Consistency by Area", "Parent Insights")
                tabs.forEachIndexed { index, label ->
                    val isSelected = selectedViewIndex == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) AbleyInk else Color.Transparent)
                            .clickable { selectedViewIndex = index }
                            .padding(vertical = 8.dp)
                            .testTag("grow_dashboard_tab_$index"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontFamily = DmSansFontFamily,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp,
                            color = if (isSelected) AbleyIvory else AbleyInk.copy(alpha = 0.7f),
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // View 0: D3-Powered Milestone Frequency Chart
            if (selectedViewIndex == 0) {
                MilestoneFrequencyD3Chart(
                    data = activeFrequencyData,
                    timeframe = frequencyTimeframe,
                    onTimeframeChanged = { frequencyTimeframe = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("milestone_frequency_chart")
                )
            } else if (selectedViewIndex == 1) {
                // View 1: Category Completion Chart
                CategoryCompletionD3Chart(
                    stats = categoryCompletionStats,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("category_completion_chart")
                )
            } else {
                // View 2: Strava-Style Parent Growth Narrative
                ParentGrowthInsightsNarrative(
                    childName = childName,
                    totalAchieved = totalAchieved,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Call-to-Action to Milestones
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onViewAllMilestones() }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AbleyTeal,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "View Clinical Milestone Breakdown",
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = AbleyTeal
                    )
                }

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

/**
 * D3-Style Milestone Frequency Chart
 * Renders a smooth bezier curve with gradient area and exact milestone point markers
 */
@Composable
fun MilestoneFrequencyD3Chart(
    data: List<FrequencyDataPoint>,
    modifier: Modifier = Modifier,
    timeframe: MilestoneFrequencyTimeframe = MilestoneFrequencyTimeframe.WEEKLY,
    onTimeframeChanged: (MilestoneFrequencyTimeframe) -> Unit = {}
) {
    var selectedPointIndex by remember(data) { mutableIntStateOf((data.size - 1).coerceAtLeast(0)) }

    Column(modifier = modifier) {
        // Top Header: Velocity Title + Weekly/Monthly Segmented Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                Text(
                    text = "ACQUISITION VELOCITY",
                    fontFamily = DmSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = AbleyInk.copy(alpha = 0.5f),
                    letterSpacing = 1.sp
                )
                Text(
                    text = if (data.isNotEmpty() && selectedPointIndex in data.indices) {
                        "${data[selectedPointIndex].label} (${data[selectedPointIndex].periodName})"
                    } else {
                        "Milestone Cadence"
                    },
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = AbleyTeal
                )
            }

            // Segmented Pill Toggle: Weekly vs Monthly (Premium Ivory / Teal Aesthetic)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(AbleyIvory)
                    .border(1.dp, AbleyTeal.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                    .padding(3.dp)
                    .testTag("frequency_timeframe_toggle"),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isWeekly = timeframe == MilestoneFrequencyTimeframe.WEEKLY
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isWeekly) AbleyTeal else Color.Transparent)
                        .clickable { onTimeframeChanged(MilestoneFrequencyTimeframe.WEEKLY) }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("frequency_toggle_weekly"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Weekly",
                        fontFamily = DmSansFontFamily,
                        fontWeight = if (isWeekly) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.sp,
                        color = if (isWeekly) AbleyIvory else AbleyInk.copy(alpha = 0.65f)
                    )
                }

                val isMonthly = timeframe == MilestoneFrequencyTimeframe.MONTHLY
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isMonthly) AbleyTeal else Color.Transparent)
                        .clickable { onTimeframeChanged(MilestoneFrequencyTimeframe.MONTHLY) }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("frequency_toggle_monthly"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Monthly",
                        fontFamily = DmSansFontFamily,
                        fontWeight = if (isMonthly) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.sp,
                        color = if (isMonthly) AbleyIvory else AbleyInk.copy(alpha = 0.65f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Velocity & Cadence Insight Pill
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (timeframe == MilestoneFrequencyTimeframe.WEEKLY) {
                    "Cadence: 3.6 milestones / week"
                } else {
                    "Cadence: 11.6 milestones / month"
                },
                fontFamily = DmSansFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                color = AbleyInk.copy(alpha = 0.65f)
            )

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (timeframe == MilestoneFrequencyTimeframe.WEEKLY) AbleyCoralLight else AbleyTealLight
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = if (timeframe == MilestoneFrequencyTimeframe.WEEKLY) AbleyCoral else AbleyTeal,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = if (timeframe == MilestoneFrequencyTimeframe.WEEKLY) "+28% vs Last Month" else "+45% Sustained Momentum",
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = if (timeframe == MilestoneFrequencyTimeframe.WEEKLY) AbleyCoral else AbleyTeal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Canvas D3-aesthetic chart area with Ivory/Teal theme
        val maxCount = if (timeframe == MilestoneFrequencyTimeframe.WEEKLY) 6f else 20f

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AbleyIvory)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val pointCount = data.size
                val stepX = width / (pointCount - 1).coerceAtLeast(1)

                // D3 Grid lines (Horizontal dashed lines at 1/3, 2/3, 1)
                listOf(0.33f, 0.66f, 1f).forEach { fraction ->
                    val y = height * (1f - fraction)
                    drawLine(
                        color = Color(0xFFE2DDD5),
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                    )
                }

                // Compute points
                val points = data.mapIndexed { index, point ->
                    val x = index * stepX
                    val y = height - (point.count / maxCount) * (height * 0.82f) - 10f
                    Offset(x, y)
                }

                // Draw Area Gradient under the curve
                val areaPath = Path().apply {
                    if (points.isNotEmpty()) {
                        moveTo(points.first().x, height)
                        lineTo(points.first().x, points.first().y)
                        for (i in 0 until points.size - 1) {
                            val p1 = points[i]
                            val p2 = points[i + 1]
                            val controlPoint1 = Offset(p1.x + (p2.x - p1.x) / 2f, p1.y)
                            val controlPoint2 = Offset(p1.x + (p2.x - p1.x) / 2f, p2.y)
                            cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, p2.x, p2.y)
                        }
                        lineTo(points.last().x, height)
                        close()
                    }
                }

                drawPath(
                    path = areaPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            AbleyTeal.copy(alpha = 0.28f),
                            AbleyTeal.copy(alpha = 0.04f)
                        )
                    )
                )

                // Draw Cubic Bezier Smooth Line
                val linePath = Path().apply {
                    if (points.isNotEmpty()) {
                        moveTo(points.first().x, points.first().y)
                        for (i in 0 until points.size - 1) {
                            val p1 = points[i]
                            val p2 = points[i + 1]
                            val controlPoint1 = Offset(p1.x + (p2.x - p1.x) / 2f, p1.y)
                            val controlPoint2 = Offset(p1.x + (p2.x - p1.x) / 2f, p2.y)
                            cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, p2.x, p2.y)
                        }
                    }
                }

                drawPath(
                    path = linePath,
                    color = AbleyTeal,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )

                // Draw circular milestone markers
                points.forEachIndexed { index, point ->
                    val isSelected = index == selectedPointIndex
                    if (isSelected) {
                        drawCircle(
                            color = AbleyCoral.copy(alpha = 0.25f),
                            radius = 9.dp.toPx(),
                            center = point
                        )
                        drawCircle(
                            color = AbleyCoral,
                            radius = 5.dp.toPx(),
                            center = point
                        )
                    } else {
                        drawCircle(
                            color = Color.White,
                            radius = 4.5.dp.toPx(),
                            center = point
                        )
                        drawCircle(
                            color = AbleyTeal,
                            radius = 4.5.dp.toPx(),
                            center = point,
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // X-Axis Labels (Clickable to inspect each period)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.forEachIndexed { index, point ->
                val isSelected = index == selectedPointIndex
                Text(
                    text = point.weekShort,
                    fontFamily = DmSansFontFamily,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 11.sp,
                    color = if (isSelected) AbleyCoral else AbleyInk.copy(alpha = 0.55f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { selectedPointIndex = index }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

/**
 * D3-Style Category Completion Chart
 * Shows horizontal multi-bar breakdown across key developmental pillars
 */
@Composable
fun CategoryCompletionD3Chart(
    stats: List<CategoryCompletionStat>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        stats.forEach { stat ->
            val percentage = ((stat.achieved.toFloat() / stat.total.toFloat()) * 100).toInt()
            val animatedFraction by animateFloatAsState(
                targetValue = stat.achieved.toFloat() / stat.total.toFloat(),
                animationSpec = tween(durationMillis = 600),
                label = "cat_anim_${stat.name}"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AbleyIvory)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = stat.icon, fontSize = 16.sp)
                        Column {
                            Text(
                                text = stat.name,
                                fontFamily = DmSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = AbleyInk
                            )
                            Text(
                                text = stat.domain,
                                fontFamily = DmSansFontFamily,
                                fontSize = 10.sp,
                                color = AbleyInk.copy(alpha = 0.55f)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "${stat.achieved}/${stat.total}",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = AbleyInk.copy(alpha = 0.7f)
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = stat.color.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "$percentage%",
                                fontFamily = DmSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = stat.color,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { animatedFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = stat.color,
                    trackColor = AbleySand,
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}

/**
 * Strava-Style Non-Comparative Parent Growth Insights
 */
@Composable
fun ParentGrowthInsightsNarrative(
    childName: String,
    totalAchieved: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(AbleySand.copy(alpha = 0.5f))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(AbleyTeal),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = AbleyIvory,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column {
                Text(
                    text = "Pediatric Growth Synthesis",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = AbleyInk
                )
                Text(
                    text = "Strava-Style Intentional Family Progress",
                    fontFamily = DmSansFontFamily,
                    fontSize = 11.sp,
                    color = AbleyTeal,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Text(
            text = "$childName is demonstrating strong developmental momentum, particularly in bilateral balance and receptive speech. $totalAchieved verified milestones achieved through intentional daily family movement routines.",
            fontFamily = DmSansFontFamily,
            fontSize = 12.sp,
            color = AbleyInk.copy(alpha = 0.85f),
            lineHeight = 18.sp
        )

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color.White
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = AbleyCoral,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Zero percentiles or comparison anxiety · Pure family celebration",
                    fontFamily = DmSansFontFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = AbleyInk.copy(alpha = 0.7f)
                )
            }
        }
    }
}

data class FrequencyDataPoint(
    val weekShort: String,
    val periodName: String,
    val count: Int,
    val label: String
)

data class CategoryCompletionStat(
    val name: String,
    val domain: String,
    val achieved: Int,
    val total: Int,
    val color: Color,
    val icon: String
)
