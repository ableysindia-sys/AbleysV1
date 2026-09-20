package com.example.ui.screens.story

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildProfile
import com.example.data.model.MemoryItem
import com.example.data.model.MemoryType
import com.example.data.model.ShareCardData
import com.example.data.model.ShareCardTheme
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleyCoralLight
import com.example.ui.theme.AbleyInk
import com.example.ui.theme.AbleyIvory
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal
import com.example.ui.theme.DmSansFontFamily
import com.example.ui.theme.PoppinsFontFamily
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

/**
 * Story Tab: The Emotional Retention Engine
 * Displays a chronological feed of child milestones and placeholders for uploaded photos.
 * Implemented with Ivory/Sand palette and DM Sans typography.
 */
@Composable
fun StoryScreen(
    childProfile: ChildProfile?,
    memories: List<MemoryItem>,
    onAddMomentClick: () -> Unit,
    onShareMemory: (ShareCardData) -> Unit,
    modifier: Modifier = Modifier
) {
    val childName = childProfile?.name ?: "Aarav"
    var selectedFilter by remember { mutableStateOf("All Feed") }
    // Timeline / Months / Year -- the three Memories surfaces the spec names.
    var viewMode by remember { mutableStateOf(StoryViewMode.TIMELINE) }

    // Sort memories chronologically (most recent first)
    val chronologicalMemories = remember(memories) {
        memories.sortedByDescending { it.timestamp }
    }

    val monthPeriods = remember(chronologicalMemories) { groupIntoMonths(chronologicalMemories) }
    val yearPeriods = remember(chronologicalMemories) { groupIntoYears(chronologicalMemories) }

    val filteredMemories = remember(selectedFilter, chronologicalMemories) {
        when (selectedFilter) {
            "Uploaded Photos" -> chronologicalMemories.filter { it.type == MemoryType.PARENT_ADDED }
            "Milestones" -> chronologicalMemories.filter { it.type == MemoryType.SYSTEM_GENERATED }
            else -> chronologicalMemories
        }
    }

    Scaffold(
        containerColor = AbleyIvory,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddMomentClick,
                containerColor = AbleyCoral,
                contentColor = AbleyIvory,
                shape = CircleShape,
                modifier = Modifier.testTag("fab_add_moment")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Childhood Memory",
                    modifier = Modifier.size(26.dp)
                )
            }
        },
        modifier = modifier.testTag("story_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AbleyIvory)
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .testTag("story_feed"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Block
            item {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = "ABLEY'S STORY",
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp,
                        color = AbleyTeal
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Story & Timeline",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = AbleyInk
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "The emotional anchor · A running chronological feed of milestones & family photos",
                        fontFamily = DmSansFontFamily,
                        fontSize = 13.sp,
                        color = AbleyInk.copy(alpha = 0.7f),
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Emotional Retention Callout Card (Ivory/Sand Aesthetic)
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = AbleySand),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(AbleyCoralLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = AbleyCoral,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "365 Moments Captured",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = AbleyInk
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "30 Days Moving Together · 186 Minutes · Real Childhood Joy",
                                    fontFamily = DmSansFontFamily,
                                    fontSize = 12.sp,
                                    color = AbleyInk.copy(alpha = 0.75f),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            // Quick Add Photo Prompt Card (Inline Invitation)
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AbleySand),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onAddMomentClick() }
                        .testTag("prompt_add_photo")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(AbleySand),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = "Add Photo",
                                tint = AbleyInk,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Capture Today's Memory",
                                fontFamily = DmSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = AbleyInk
                            )
                            Text(
                                text = "Upload a photo from today's movement or playtime",
                                fontFamily = DmSansFontFamily,
                                fontSize = 11.sp,
                                color = AbleyInk.copy(alpha = 0.6f)
                            )
                        }
                        Text(
                            text = "+ Upload",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = AbleyCoral
                        )
                    }
                }
            }

            // Filter Chips (Using DM Sans)
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("All Feed", "Milestones", "Uploaded Photos").forEach { filter ->
                        val isSelected = selectedFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilter = filter },
                            label = {
                                Text(
                                    filter,
                                    fontFamily = DmSansFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AbleyInk,
                                selectedLabelColor = AbleyIvory,
                                containerColor = Color.White,
                                labelColor = AbleyInk
                            ),
                            modifier = Modifier.testTag("story_filter_$filter")
                        )
                    }
                }
            }

            item {
                StoryViewModeSelector(
                    selected = viewMode,
                    onSelect = { viewMode = it }
                )
            }

            if (viewMode != StoryViewMode.TIMELINE) {
                item {
                    if (viewMode == StoryViewMode.MONTHS) {
                        MemoryMonthView(
                            periods = monthPeriods,
                            onSharePeriod = { period ->
                                onShareMemory(
                                    ShareCardData(
                                        title = period.label.uppercase(),
                                        bigNumber = "${period.memories.size}",
                                        unitLabel = "MOMENTS CAPTURED",
                                        statsSubtitle = "${period.photoCount} photos \u00b7 " +
                                            "${period.autoCount} added by Abley's",
                                        childName = childName,
                                        ageOrYear = period.year.toString(),
                                        theme = ShareCardTheme.CORAL_PRIDE
                                    )
                                )
                            }
                        )
                    } else {
                        MemoryYearView(
                            periods = yearPeriods,
                            onSharePeriod = { period ->
                                onShareMemory(
                                    ShareCardData(
                                        title = "YEAR IN GROWING",
                                        bigNumber = "${period.memories.size}",
                                        unitLabel = "MOMENTS",
                                        statsSubtitle = "${period.photoCount} photos \u00b7 " +
                                            "${period.parentCount} captured by you",
                                        childName = childName,
                                        ageOrYear = period.year.toString(),
                                        theme = ShareCardTheme.SAND_EDITORIAL
                                    )
                                )
                            }
                        )
                    }
                }
            }

            // Timeline Feed Section Header
            if (viewMode == StoryViewMode.TIMELINE) item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CHRONOLOGICAL FEED",
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 1.2.sp,
                        color = AbleyInk.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "${filteredMemories.size} entries",
                        fontFamily = DmSansFontFamily,
                        fontSize = 11.sp,
                        color = AbleyInk.copy(alpha = 0.5f)
                    )
                }
            }

            // Chronological Items Feed
            items(
                if (viewMode == StoryViewMode.TIMELINE) filteredMemories else emptyList(),
                key = { it.id }
            ) { memory ->
                if (memory.type == MemoryType.SYSTEM_GENERATED) {
                    MilestoneFeedCard(
                        memory = memory,
                        childName = childName,
                        onShareClick = {
                            onShareMemory(
                                ShareCardData(
                                    title = "DEVELOPMENTAL MILESTONE",
                                    bigNumber = memory.emojiTag,
                                    unitLabel = memory.title.uppercase(),
                                    statsSubtitle = "${memory.caption}\nCaptured on ${memory.dateString}",
                                    childName = childName,
                                    ageOrYear = "Age 5",
                                    theme = ShareCardTheme.TEAL_MOVEMENT,
                                    footerMessage = "CELEBRATED IN ABLEY'S →"
                                )
                            )
                        }
                    )
                } else {
                    PhotoMemoryFeedCard(
                        memory = memory,
                        childName = childName,
                        onShareClick = {
                            onShareMemory(
                                ShareCardData(
                                    title = "CHILDHOOD MEMORY",
                                    bigNumber = memory.emojiTag,
                                    unitLabel = memory.title.uppercase(),
                                    statsSubtitle = "${memory.caption}\nCaptured on ${memory.dateString}",
                                    childName = childName,
                                    ageOrYear = "Age 5",
                                    theme = ShareCardTheme.CORAL_PRIDE,
                                    footerMessage = "SAVED IN ABLEY'S STORY →"
                                )
                            )
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}

/**
 * Milestone Card in the Chronological Feed
 * Styled with Ivory/Sand theme and DM Sans typography
 */
@Composable
fun MilestoneFeedCard(
    memory: MemoryItem,
    childName: String,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AbleySand),
        modifier = modifier
            .fillMaxWidth()
            .testTag("memory_card_${memory.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header Row: Milestone Badge + Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AbleyTeal.copy(alpha = 0.12f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AbleyTeal,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "ABLEY'S MILESTONE",
                            color = AbleyTeal,
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Text(
                    text = memory.dateString,
                    fontFamily = DmSansFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    color = AbleyInk.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Emoji Icon & Milestone Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(AbleyTeal.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = memory.emojiTag,
                        fontSize = 22.sp
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = memory.title,
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AbleyInk
                    )
                    Text(
                        text = "Developmental milestone verified",
                        fontFamily = DmSansFontFamily,
                        fontSize = 11.sp,
                        color = AbleyTeal,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Caption Description in DM Sans
            Text(
                text = memory.caption,
                fontFamily = DmSansFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = AbleyInk.copy(alpha = 0.8f),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action Row: Share Milestone Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onShareClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AbleySand,
                        contentColor = AbleyInk
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("share_memory_${memory.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Card",
                        tint = AbleyInk,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Share Milestone",
                        fontFamily = DmSansFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AbleyInk
                    )
                }
            }
        }
    }
}

/**
 * Uploaded Photo Memory Card with visual photo placeholder
 * Styled with Ivory/Sand theme and DM Sans typography
 */
@Composable
fun PhotoMemoryFeedCard(
    memory: MemoryItem,
    childName: String,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPhotoEnlarged by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AbleySand),
        modifier = modifier
            .fillMaxWidth()
            .testTag("memory_card_${memory.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header Row: Parent Moment Badge + Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AbleyCoralLight
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = AbleyCoral,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "PARENT PHOTO MEMORY",
                            color = AbleyCoral,
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Text(
                    text = memory.dateString,
                    fontFamily = DmSansFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    color = AbleyInk.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Memory Title in DM Sans
            Text(
                text = memory.title,
                fontFamily = DmSansFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = AbleyInk
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Placeholder for Uploaded Photo (Ivory/Sand styled frame)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isPhotoEnlarged) 260.dp else 195.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AbleySand,
                                AbleySand.copy(alpha = 0.85f),
                                AbleyIvory
                            )
                        )
                    )
                    .border(1.dp, AbleySand, RoundedCornerShape(16.dp))
                    .clickable { isPhotoEnlarged = !isPhotoEnlarged }
                    .testTag("photo_placeholder_${memory.id}"),
                contentAlignment = Alignment.Center
            ) {
                val storedPhoto = memory.photoUri
                if (storedPhoto != null) {
                    AsyncImage(
                        model = storedPhoto,
                        contentDescription = memory.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("memory_photo_${memory.id}")
                    )
                }
                // Shown when the memory has no photo of its own, and behind nothing when it does.
                if (storedPhoto == null) Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.85f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = memory.emojiTag,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Childhood Moment Photo",
                        fontFamily = DmSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = AbleyInk
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Captured on ${memory.dateString} · Tap to ${if (isPhotoEnlarged) "minimize" else "enlarge"}",
                        fontFamily = DmSansFontFamily,
                        fontSize = 11.sp,
                        color = AbleyInk.copy(alpha = 0.6f)
                    )
                }

                // Top Badge: Photo attached indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = AbleyInk.copy(alpha = 0.7f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = null,
                                tint = AbleyIvory,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = "PHOTO ATTACHED",
                                fontFamily = DmSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = AbleyIvory
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = AbleySand
                    ) {
                        Text(
                            text = "Family Album",
                            fontFamily = DmSansFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 9.sp,
                            color = AbleyInk,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                // Bottom Overlay Bar on photo placeholder
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(AbleyInk.copy(alpha = 0.6f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Saved in $childName's Private Vault",
                        fontFamily = DmSansFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = AbleyIvory
                    )
                    Text(
                        text = "100% Private",
                        fontFamily = DmSansFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AbleyCoralLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Caption in DM Sans
            Text(
                text = memory.caption,
                fontFamily = DmSansFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = AbleyInk.copy(alpha = 0.85f),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Captured by Parents · Age 5",
                fontFamily = DmSansFontFamily,
                fontSize = 11.sp,
                color = AbleyInk.copy(alpha = 0.5f),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action: Share Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onShareClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AbleySand,
                        contentColor = AbleyInk
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("share_memory_${memory.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Memory",
                        tint = AbleyInk,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Share Memory",
                        fontFamily = DmSansFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AbleyInk
                    )
                }
            }
        }
    }
}

/** The three Memories surfaces named in the spec. */
enum class StoryViewMode(val label: String) {
    TIMELINE("Timeline"),
    MONTHS("Months"),
    YEAR("Year")
}

@Composable
private fun StoryViewModeSelector(
    selected: StoryViewMode,
    onSelect: (StoryViewMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(AbleySand.copy(alpha = 0.45f))
            .padding(4.dp)
            .testTag("story_view_mode_selector"),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        StoryViewMode.entries.forEach { mode ->
            val isSelected = mode == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(11.dp))
                    .background(if (isSelected) Color.White else Color.Transparent)
                    .clickable { onSelect(mode) }
                    .padding(vertical = 9.dp)
                    .testTag("story_mode_${mode.name.lowercase()}"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mode.label,
                    fontFamily = DmSansFontFamily,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) AbleyInk else AbleyInk.copy(alpha = 0.55f)
                )
            }
        }
    }
}
