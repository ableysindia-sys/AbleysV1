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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Diversity1
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildProfile
import com.example.data.model.ParentStory
import com.example.data.model.TherapyArea
import com.example.data.model.TherapyProgram
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleyCoralLight
import com.example.ui.theme.AbleyInk
import com.example.ui.theme.AbleyIvory
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal
import com.example.ui.theme.DmSansFontFamily
import com.example.ui.theme.PoppinsFontFamily
import com.example.viewmodel.SupportSubTab

@Composable
fun SupportScreen(
    childProfile: ChildProfile?,
    currentSubTab: SupportSubTab,
    onSelectSubTab: (SupportSubTab) -> Unit,
    therapyPrograms: List<TherapyProgram>,
    parentStories: List<ParentStory>,
    onSelectProgram: (TherapyProgram) -> Unit,
    onSelectStory: (ParentStory) -> Unit,
    onStartSession: (TherapyProgram) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val childName = childProfile?.name ?: "Aarav"
    var selectedTherapyAreaFilter by remember { mutableStateOf<TherapyArea?>(null) }
    var selectedTopicFilter by remember { mutableStateOf("All") }

    val filteredPrograms = remember(selectedTherapyAreaFilter, therapyPrograms) {
        if (selectedTherapyAreaFilter == null) therapyPrograms
        else therapyPrograms.filter { it.area == selectedTherapyAreaFilter }
    }

    val filteredStories = remember(selectedTopicFilter, parentStories) {
        if (selectedTopicFilter == "All") parentStories
        else parentStories.filter { it.topic.equals(selectedTopicFilter, ignoreCase = true) }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AbleyIvory)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = "ABLEY'S SUPPORT",
                    fontFamily = DmSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 1.5.sp,
                    color = AbleyTeal
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Clinical Therapy",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = AbleyInk
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pediatric PT/OT therapy loops with structured movement & sensory regulation",
                    fontFamily = DmSansFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = AbleyInk.copy(alpha = 0.7f),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Sub-tabs (Therapy at Home vs Parent-to-Parent)
                TabRow(
                    selectedTabIndex = if (currentSubTab == SupportSubTab.THERAPY) 0 else 1,
                    containerColor = Color.White,
                    contentColor = AbleyTeal,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[if (currentSubTab == SupportSubTab.THERAPY) 0 else 1]),
                            color = AbleyTeal,
                            height = 3.dp
                        )
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .testTag("support_sub_tab_row")
                ) {
                    Tab(
                        selected = currentSubTab == SupportSubTab.THERAPY,
                        onClick = { onSelectSubTab(SupportSubTab.THERAPY) },
                        text = {
                            Text(
                                text = "Therapy at Home",
                                fontFamily = DmSansFontFamily,
                                fontWeight = if (currentSubTab == SupportSubTab.THERAPY) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        selectedContentColor = AbleyTeal,
                        unselectedContentColor = AbleyInk.copy(alpha = 0.6f)
                    )

                    Tab(
                        selected = currentSubTab == SupportSubTab.PARENT_STORIES,
                        onClick = { onSelectSubTab(SupportSubTab.PARENT_STORIES) },
                        text = {
                            Text(
                                text = "Parent-to-Parent",
                                fontFamily = DmSansFontFamily,
                                fontWeight = if (currentSubTab == SupportSubTab.PARENT_STORIES) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Diversity1,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        selectedContentColor = AbleyCoral,
                        unselectedContentColor = AbleyInk.copy(alpha = 0.6f)
                    )
                }
            }
        }

        if (currentSubTab == SupportSubTab.THERAPY) {
            // Clinical Rigor Hero Banner
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = AbleyTeal),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HealthAndSafety,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "CLINICAL RIGOR · PEDIATRIC CERTIFIED",
                                fontFamily = DmSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Created by qualified pediatric PT/OTs",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Home therapy loops designed with clear boundaries for developmental delays, vestibular calibration, and sensory processing.",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            // Key clinical areas filter chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = selectedTherapyAreaFilter == null,
                            onClick = { selectedTherapyAreaFilter = null },
                            label = {
                                Text(
                                    "All Areas",
                                    fontFamily = DmSansFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedTherapyAreaFilter == null) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AbleyTeal,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = AbleyInk
                            )
                        )
                    }

                    items(TherapyArea.entries.toTypedArray()) { area ->
                        val isSelected = selectedTherapyAreaFilter == area
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedTherapyAreaFilter = area },
                            label = {
                                Text(
                                    area.displayName,
                                    fontFamily = DmSansFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AbleyTeal,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = AbleyInk
                            )
                        )
                    }
                }
            }

            // Clinical Therapy Session Loops List
            items(filteredPrograms, key = { it.id }) { program ->
                TherapyProgramCard(
                    program = program,
                    onStartSession = { onStartSession(program) },
                    onViewProtocol = { onSelectProgram(program) }
                )
            }
        } else {
            // Parent-to-Parent Tab Content
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = AbleySand.copy(alpha = 0.7f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "THE LIVED EXPERIENCE LAYER",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp,
                            color = AbleyCoral
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Peer-to-peer knowledge alongside clinical guidance",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = AbleyInk
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Real stories, real tips, and encouragement from parents navigating the exact same path.",
                            fontFamily = DmSansFontFamily,
                            fontSize = 12.sp,
                            color = AbleyInk.copy(alpha = 0.75f),
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            // Topics filter chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listOf("All", "Equipment", "Milestones", "Daily Routines", "Regulation")) { topic ->
                        val isSelected = selectedTopicFilter == topic
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedTopicFilter = topic },
                            label = {
                                Text(
                                    topic,
                                    fontFamily = DmSansFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AbleyCoral,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = AbleyInk
                            )
                        )
                    }
                }
            }

            // Stories list
            items(filteredStories, key = { it.id }) { story ->
                ParentStoryCard(
                    story = story,
                    onClick = { onSelectStory(story) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * Clinical Therapy Session Loop Item
 * Features:
 * 1. Video placeholder with interactive looping demo preview
 * 2. Brief description with target outcomes and clinical safety guidance
 * 3. 'Start Session' action button using the Teal/Coral color palette
 */
@Composable
fun TherapyProgramCard(
    program: TherapyProgram,
    onStartSession: () -> Unit,
    onViewProtocol: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isDemoPreviewActive by remember { mutableStateOf(false) }
    var isCountdownTimerOverlayVisible by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("therapy_card_${program.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header Row: Area Badge + Week/Session + Duration
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AbleyTeal.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = program.area.displayName.uppercase(),
                        color = AbleyTeal,
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "WK ${program.weekNumber} · S${program.sessionNumber}",
                        fontFamily = DmSansFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AbleyInk.copy(alpha = 0.5f)
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isCountdownTimerOverlayVisible) AbleyCoral else AbleySand.copy(alpha = 0.6f),
                        modifier = Modifier
                            .clickable { isCountdownTimerOverlayVisible = !isCountdownTimerOverlayVisible }
                            .testTag("therapy_duration_badge_${program.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Toggle Countdown Timer",
                                tint = if (isCountdownTimerOverlayVisible) AbleyIvory else AbleyInk.copy(alpha = 0.6f),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "${program.durationMinutes}m loop",
                                fontFamily = DmSansFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCountdownTimerOverlayVisible) AbleyIvory else AbleyInk
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 1. Video Placeholder (16:9 Clinical Demo Loop Frame) with Countdown Timer Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isCountdownTimerOverlayVisible) 205.dp else 185.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AbleySand,
                                AbleySand.copy(alpha = 0.85f),
                                AbleyTeal.copy(alpha = 0.12f)
                            )
                        )
                    )
                    .border(1.dp, AbleySand, RoundedCornerShape(16.dp))
                    .testTag("video_placeholder_${program.id}"),
                contentAlignment = Alignment.Center
            ) {
                if (isCountdownTimerOverlayVisible) {
                    TherapyCountdownTimerOverlay(
                        durationMinutes = program.durationMinutes,
                        programTitle = program.title,
                        clinicalTarget = program.clinicalTarget,
                        onClose = { isCountdownTimerOverlayVisible = false },
                        onLaunchFullSession = onStartSession,
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("therapy_countdown_timer_overlay_${program.id}")
                    )
                } else {
                    // Top Overlay: Clinical Badge and HD Indicator + Countdown Timer Overlay Pill
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = AbleyInk.copy(alpha = 0.78f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (isDemoPreviewActive) AbleyCoral else Color(0xFF4CAF50))
                                )
                                Text(
                                    text = "CLINICAL DEMO LOOP",
                                    fontFamily = DmSansFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    color = AbleyIvory,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Dedicated Countdown Timer Overlay Toggle Pill
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = AbleyTeal,
                                modifier = Modifier
                                    .clickable { isCountdownTimerOverlayVisible = true }
                                    .testTag("toggle_countdown_overlay_${program.id}")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = "Open Countdown Timer",
                                        tint = AbleyIvory,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Text(
                                        text = "TIMER OVERLAY",
                                        fontFamily = DmSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        color = AbleyIvory,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = AbleyInk.copy(alpha = 0.6f)
                            ) {
                                Text(
                                    text = "1080p HD",
                                    fontFamily = DmSansFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    color = AbleyIvory,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // Center: Interactive Play Control & State
                    if (!isDemoPreviewActive) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.clickable { isDemoPreviewActive = true }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(AbleyTeal)
                                    .border(2.dp, AbleyCoral.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play Clinical Demo",
                                    tint = AbleyIvory,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Tap to preview 3D clinical demo loop",
                                fontFamily = DmSansFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp,
                                color = AbleyInk.copy(alpha = 0.7f)
                            )
                        }
                    } else {
                        // Active looping preview simulation
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isDemoPreviewActive = false }
                                .padding(horizontal = 16.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = AbleyTeal.copy(alpha = 0.92f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Videocam,
                                        contentDescription = null,
                                        tint = AbleyIvory,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Playing Clinical Demo · Step 1 of ${program.steps.size.coerceAtLeast(3)}",
                                        fontFamily = DmSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = AbleyIvory
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tap anywhere to pause preview",
                                fontFamily = DmSansFontFamily,
                                fontSize = 10.sp,
                                color = AbleyInk.copy(alpha = 0.6f)
                            )
                        }
                    }

                    // Bottom Overlay: Structured Therapy Loop Phases
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(AbleyInk.copy(alpha = 0.72f))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "1. Sensory Warm-Up",
                            fontFamily = DmSansFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = AbleyIvory
                        )
                        Text(
                            text = "➔",
                            fontSize = 9.sp,
                            color = AbleyCoral
                        )
                        Text(
                            text = "2. Active Loop",
                            fontFamily = DmSansFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AbleyIvory
                        )
                        Text(
                            text = "➔",
                            fontSize = 9.sp,
                            color = AbleyCoral
                        )
                        Text(
                            text = "3. Calming Transition",
                            fontFamily = DmSansFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = AbleyIvory
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title
            Text(
                text = program.title,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                color = AbleyInk,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Clinical Target (Clear physiological objective)
            Text(
                text = "Target: ${program.clinicalTarget}",
                fontFamily = DmSansFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = AbleyCoral
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 2. Brief Description (Evidence-based clinical protocol summary)
            Text(
                text = program.description,
                fontFamily = DmSansFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = AbleyInk.copy(alpha = 0.75f),
                lineHeight = 19.sp
            )

            // Equipment Needed Callout (if applicable)
            val equip = program.equipmentName
            if (!equip.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AbleySand.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = AbleyTeal,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Equipment: $equip",
                            fontFamily = DmSansFontFamily,
                            color = AbleyInk,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Action Row: 'Start Session' Button (Teal/Coral Palette) + Protocol CTA
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Primary Action: Start Session (Teal / Coral Palette)
                Button(
                    onClick = onStartSession,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AbleyTeal,
                        contentColor = AbleyIvory
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("start_session_${program.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Coral play icon accent circle
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(AbleyCoral),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = AbleyIvory,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "START SESSION",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = AbleyIvory,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Secondary Action: Clinical Protocol & Safety Details
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = AbleySand.copy(alpha = 0.6f),
                    modifier = Modifier
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { onViewProtocol() }
                        .testTag("view_protocol_${program.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .padding(horizontal = 14.dp)
                            .fillMaxHeight()
                    ) {
                        Text(
                            text = "Protocol",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = AbleyInk
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "View Protocol",
                            tint = AbleyInk,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ParentStoryCard(
    story: ParentStory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag("parent_story_card_${story.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AbleyCoralLight
                ) {
                    Text(
                        text = story.topic.uppercase(),
                        color = AbleyCoral,
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = AbleyCoral,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${story.helpfulCount} helpful",
                        fontFamily = DmSansFontFamily,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = AbleyInk.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = story.title,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = AbleyInk
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (story.isPlaceholder) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AbleyInk.copy(alpha = 0.08f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "SAMPLE STORY · NOT A REAL FAMILY",
                        fontFamily = DmSansFontFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = AbleyInk.copy(alpha = 0.6f)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = "By ${story.authorName} (${story.authorRole})",
                fontFamily = DmSansFontFamily,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = AbleyTeal,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = story.excerpt,
                fontFamily = DmSansFontFamily,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = AbleyInk.copy(alpha = 0.75f),
                    lineHeight = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Read Parent Story",
                    fontFamily = DmSansFontFamily,
                    color = AbleyCoral,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = AbleyCoral,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

