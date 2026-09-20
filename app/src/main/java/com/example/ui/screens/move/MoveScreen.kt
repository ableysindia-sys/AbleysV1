package com.example.ui.screens.move

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildProfile
import com.example.data.model.EquipmentProduct
import com.example.data.model.MoveActivity
import com.example.ui.theme.AbleyBorder
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleyCoralLight
import com.example.ui.theme.AbleyInk
import com.example.ui.theme.AbleyIvory
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal
import com.example.ui.theme.AbleyTealLight
import com.example.ui.theme.DmSansFontFamily
import com.example.ui.theme.PoppinsFontFamily
import com.example.data.model.MoveProgram
import com.example.data.model.SkillArea
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import com.example.data.model.SpaceNeeded

@Composable
fun MoveScreen(
    childProfile: ChildProfile?,
    activities: List<MoveActivity>,
    onSelectActivity: (MoveActivity) -> Unit,
    equipmentList: List<EquipmentProduct> = emptyList(),
    onOpenShopItem: (String) -> Unit = {},
    onStartActivityNow: (MoveActivity) -> Unit = onSelectActivity,
    programs: List<MoveProgram> = emptyList(),
    programProgress: Map<String, Int> = emptyMap(),
    onSelectProgram: (MoveProgram) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val childName = childProfile?.name ?: "Aarav"
    var selectedFormatFilter by remember { mutableStateOf("All") }
    var selectedAreaFilter by remember { mutableStateOf<SkillArea?>(null) }
    // Smog days in the north and the monsoon everywhere else close outdoor play for weeks. On
    // those days the only question is what can be done in a flat.
    var indoorOnly by remember { mutableStateOf(false) }

    // One Week and One Month were dropped from this row: those formats belong to the programmes
    // rail above, so as activity filters they could only ever return nothing.
    val formatFilters = listOf("All", "Quick Bursts", "Daily Rituals", "Indoor Agility")

    val filteredActivities = remember(selectedFormatFilter, selectedAreaFilter, indoorOnly, activities) {
        activities
            .filter { !indoorOnly || it.spaceNeeded != SpaceNeeded.OPEN_SPACE }
            .filter { activity ->
                val area = selectedAreaFilter ?: return@filter true
                activity.targetArea == area.id
            }
            .filter { activity ->
                if (selectedFormatFilter == "All") return@filter true
                val query = when (selectedFormatFilter) {
                    "Quick Bursts" -> "Quick"
                    "Daily Rituals" -> "Daily"
                    "Indoor Agility" -> "Anywhere"
                    else -> selectedFormatFilter
                }
                activity.categoryBadge.contains(query, ignoreCase = true) ||
                    activity.format.name.contains(query, ignoreCase = true)
            }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AbleyIvory)
            .padding(horizontal = 20.dp)
            .testTag("move_screen"),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Screen Header & Strava-Style Dedication Card
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                // Editorial Label
                Text(
                    text = "ABLEY'S MOVE",
                    fontFamily = DmSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = AbleyTeal,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Display Header in Poppins
                Text(
                    text = "Move Together",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = AbleyInk,
                    lineHeight = 38.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Designed with pediatric OTs & PTs · Active space engagement",
                    fontFamily = DmSansFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = AbleyInk.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Daily Streak Counter Widget (High-Contrast Coral #EE4A41 Palette)
                MoveDailyStreakCounterWidget(
                    currentStreakDays = childProfile?.currentStreak ?: 12,
                    nextMilestoneDays = 14,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Strava-Style Collaborative Dedication Card
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = AbleyTeal),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("move_strava_dedication_card")
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "30 DAYS OF MOVING TOGETHER",
                                    fontFamily = DmSansFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.85f),
                                    letterSpacing = 1.2.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "${childProfile?.minutesMoved ?: 248} Minutes Moving",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${childProfile?.activeDays ?: 18} active days · 32 shared activities · 0 sedentary screen-time",
                                    fontFamily = DmSansFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 13.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.DirectionsRun,
                                    contentDescription = "Movement",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Physical play hand-off banner
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.12f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Physical Play Hand-Off: Digital timers anchor directly to real living room space",
                                    fontFamily = DmSansFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.95f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Multi-day challenges. A programme is a commitment; a single activity is a five-minute
        // decision. They do not belong in the same list, so challenges get their own rail above it.
        if (programs.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)) {
                    Text(
                        text = "PROGRAMS & CHALLENGES",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(programs, key = { it.id }) { program ->
                            MoveProgramCard(
                                program = program,
                                daysComplete = programProgress[program.id] ?: 0,
                                onClick = { onSelectProgram(program) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (indoorOnly) AbleyTeal.copy(alpha = 0.12f) else Color.White)
                    .border(
                        1.dp,
                        if (indoorOnly) AbleyTeal else AbleySand,
                        RoundedCornerShape(14.dp)
                    )
                    .clickable { indoorOnly = !indoorOnly }
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .testTag("indoor_only_filter"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null,
                    tint = if (indoorOnly) AbleyTeal else Color.Black.copy(alpha = 0.4f),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Indoor day",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = if (indoorOnly) {
                            "Showing only what fits in a flat with the windows shut"
                        } else {
                            "Bad air or heavy rain? Hide anything that needs a park"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black.copy(alpha = 0.55f)
                    )
                }
                Switch(
                    checked = indoorOnly,
                    onCheckedChange = { indoorOnly = it },
                    colors = SwitchDefaults.colors(checkedTrackColor = AbleyTeal)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // With 46 activities in the catalogue, what a parent needs first is "which of these is
        // for the thing we are struggling with today" -- so the needs areas filter above format.
        item {
            Column {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        MoveAreaChip(
                            label = "All areas",
                            selected = selectedAreaFilter == null,
                            onClick = { selectedAreaFilter = null }
                        )
                    }
                    items(SkillArea.entries.toList(), key = { it.id }) { area ->
                        MoveAreaChip(
                            label = area.displayName,
                            selected = selectedAreaFilter == area,
                            onClick = {
                                selectedAreaFilter = if (selectedAreaFilter == area) null else area
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        // Formats filter chips (Nike Training Club style)
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(formatFilters) { filter ->
                    val isSelected = selectedFormatFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFormatFilter = filter },
                        label = {
                            Text(
                                text = filter,
                                fontFamily = DmSansFontFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AbleyCoral,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = AbleyInk
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) AbleyCoral else AbleyBorder,
                            selectedBorderColor = AbleyCoral,
                            borderWidth = 1.dp,
                            selectedBorderWidth = 1.dp,
                            enabled = true,
                            selected = isSelected
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("filter_$filter")
                    )
                }
            }
        }

        // Section Title & Activity Count
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CURATED PHYSICAL ACTIVITIES",
                    fontFamily = DmSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = AbleyInk.copy(alpha = 0.5f),
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "${filteredActivities.size} sessions",
                    fontFamily = DmSansFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = AbleyInk.copy(alpha = 0.5f)
                )
            }
        }

        // Activities List with Integrated Equipment Call-Outs & Timers
        items(filteredActivities, key = { it.id }) { activity ->
            val isEquipmentOwned = activity.equipmentSku?.let { sku ->
                equipmentList.firstOrNull { it.sku == sku }?.isOwned == true
            } ?: false

            MoveActivityCard(
                activity = activity,
                isEquipmentOwned = isEquipmentOwned,
                onCardClick = { onSelectActivity(activity) },
                onStartTimer = { onStartActivityNow(activity) },
                onShopEquipment = { sku -> onOpenShopItem(sku) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun MoveActivityCard(
    activity: MoveActivity,
    isEquipmentOwned: Boolean,
    onCardClick: () -> Unit,
    onStartTimer: () -> Unit,
    onShopEquipment: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, AbleyBorder.copy(alpha = 0.7f), RoundedCornerShape(22.dp))
            .testTag("move_activity_${activity.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header Row: Category Badge & Prominent Digital Timer Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AbleyCoralLight
                ) {
                    Text(
                        text = activity.categoryBadge.uppercase(),
                        fontFamily = DmSansFontFamily,
                        color = AbleyCoral,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                // Digital Timer Callout
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AbleyTealLight
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Timer",
                            tint = AbleyTeal,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "${activity.durationMinutes} min timer",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = AbleyTeal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Activity Title in Poppins Bold
            Text(
                text = activity.title,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = AbleyInk,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Body Description in DM Sans
            Text(
                text = activity.description,
                fontFamily = DmSansFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = AbleyInk.copy(alpha = 0.75f),
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ============================================================
            // Physical Hand-Off Equipment Call-Out (Anti-Screen Physical Anchor)
            // ============================================================
            if (activity.equipmentName != null) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = AbleySand.copy(alpha = 0.55f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("equipment_callout_${activity.id}")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "EQUIPMENT NEEDED",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = AbleyInk.copy(alpha = 0.5f),
                            letterSpacing = 1.0.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                if (isEquipmentOwned) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Equipment Owned",
                                        tint = AbleyTeal,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.RadioButtonUnchecked,
                                        contentDescription = "Equipment Needed",
                                        tint = AbleyInk.copy(alpha = 0.35f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = activity.equipmentName,
                                        fontFamily = DmSansFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = AbleyInk
                                    )
                                    if (isEquipmentOwned) {
                                        Text(
                                            text = "In family kit ✓ Ready to play",
                                            fontFamily = DmSansFontFamily,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 11.sp,
                                            color = AbleyTeal
                                        )
                                    } else {
                                        // Contextual Shop Call-out with Coral Underline
                                        Text(
                                            text = "Don't have them? Shop equipment →",
                                            fontFamily = DmSansFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = AbleyCoral,
                                            textDecoration = TextDecoration.Underline,
                                            modifier = Modifier
                                                .clickable {
                                                    activity.equipmentSku?.let { sku ->
                                                        onShopEquipment(sku)
                                                    }
                                                }
                                                .testTag("shop_equipment_${activity.equipmentSku ?: activity.id}")
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Equipment-free callout
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AbleySand.copy(alpha = 0.35f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = AbleyTeal,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "No equipment needed · Living room & bodyweight play",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = AbleyInk.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Movement Target tags (e.g. Balance, Gross motor, Coordination)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                activity.targetTags.take(3).forEach { tag ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = AbleySand
                    ) {
                        Text(
                            text = tag,
                            fontFamily = DmSansFontFamily,
                            color = AbleyInk,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Interface: Start Timer & View Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Primary Action: Start Timer (Apple Fitness+ / Nike Training Club Pill)
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = AbleyInk,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { onStartTimer() }
                        .testTag("start_timer_${activity.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = AbleyIvory,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "START ${activity.durationMinutes}-MIN TIMER",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = AbleyIvory,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Secondary View Steps CTA
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = AbleySand.copy(alpha = 0.6f),
                    modifier = Modifier
                        .height(46.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { onCardClick() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .padding(horizontal = 14.dp)
                            .fillMaxHeight()
                    ) {
                        Text(
                            text = "Guide",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = AbleyInk
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "View Steps",
                            tint = AbleyInk,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

/** Compact challenge card for the Move programmes rail. */
@Composable
private fun MoveProgramCard(
    program: MoveProgram,
    daysComplete: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = Color(program.accentColorHex)
    val started = daysComplete > 0
    Column(
        modifier = modifier
            .width(210.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, AbleySand, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(16.dp)
            .testTag("move_program_card_${program.id}")
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(accent.copy(alpha = 0.12f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "${program.totalDays} DAYS",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = accent
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = program.title,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = program.subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black.copy(alpha = 0.55f),
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (started) "Day ${daysComplete + 1} next" else "Not started",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = if (started) accent else Color.Black.copy(alpha = 0.45f)
        )
    }
}

@Composable
private fun MoveAreaChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) AbleyCoral else Color.White)
            .border(1.dp, if (selected) AbleyCoral else AbleySand, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag("move_area_chip_$label")
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (selected) Color.White else Color.Black.copy(alpha = 0.7f)
        )
    }
}
