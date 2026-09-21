package com.ableys.app.ui.screens.milestones

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ableys.app.data.model.ChildDevelopmentMilestone
import com.ableys.app.data.model.MilestoneCategory
import com.ableys.app.data.model.MilestoneProgressStatus
import com.ableys.app.ui.theme.AbleyCoral
import com.ableys.app.ui.theme.AbleyInk
import com.ableys.app.ui.theme.AbleyIvory
import com.ableys.app.ui.theme.AbleySand
import com.ableys.app.ui.theme.AbleyTeal
import com.ableys.app.viewmodel.MilestonesStats
import com.ableys.app.viewmodel.MilestonesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Screen displaying the child developmental milestone list using a LazyColumn,
 * pulling reactive data from the Room database via MilestonesViewModel.
 * Features item placement and appearance animations when new milestones are added
 * or progress status is toggled in the database.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestonesScreen(
    childName: String = "Aarav",
    onBack: (() -> Unit)? = null,
    viewModel: MilestonesViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    // Collect reactive state flows directly from the Room ViewModel
    val filteredMilestones by viewModel.filteredMilestones.collectAsStateWithLifecycle()
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedStatus by viewModel.selectedStatus.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isAddSheetOpen by viewModel.isAddDialogOpen.collectAsStateWithLifecycle()
    val selectedMilestoneForEdit by viewModel.selectedMilestoneForEdit.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = AbleyIvory,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Development Milestones",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = AbleyInk,
                                letterSpacing = (-0.3).sp
                            )
                        )
                        Text(
                            text = "$childName's Growth · ${stats.achievedCount} of ${stats.totalCount} Achieved",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = AbleyInk.copy(alpha = 0.65f),
                                fontSize = 11.sp
                            )
                        )
                    }
                },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("milestones_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = AbleyInk
                            )
                        }
                    }
                },
                actions = {
                    Button(
                        onClick = { viewModel.openAddDialog() },
                        colors = ButtonDefaults.buttonColors(containerColor = AbleyTeal),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .testTag("topbar_add_milestone_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Add",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AbleyIvory,
                    titleContentColor = AbleyInk
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.openAddDialog() },
                containerColor = AbleyTeal,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add milestone"
                    )
                },
                text = {
                    Text(
                        text = "Add Milestone",
                        fontWeight = FontWeight.Bold
                    )
                },
                modifier = Modifier.testTag("fab_add_milestone")
            )
        },
        modifier = modifier.testTag("milestones_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .testTag("milestones_lazy_column"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Overall Progress Header Card
            item(key = "progress_summary_card") {
                MilestoneScreenSummaryCard(
                    stats = stats,
                    childName = childName,
                    onAddClick = { viewModel.openAddDialog() }
                )
            }

            // 2. Search Text Field
            item(key = "search_field_item") {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = {
                        Text(
                            "Search developmental milestones...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = AbleyInk.copy(alpha = 0.45f))
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = AbleyInk.copy(alpha = 0.5f)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear search",
                                    tint = AbleyInk.copy(alpha = 0.5f)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = AbleyTeal,
                        unfocusedBorderColor = AbleySand
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("milestone_search_field")
                )
            }

            // 3. Category Filter Chips
            item(key = "category_filters_item") {
                Column {
                    Text(
                        text = "DOMAIN CATEGORY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AbleyInk.copy(alpha = 0.55f),
                            letterSpacing = 1.sp
                        ),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { viewModel.setCategoryFilter(null) },
                            label = { Text("All (${stats.totalCount})") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AbleyInk,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = AbleyInk
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("category_filter_all")
                        )

                        MilestoneCategory.entries.forEach { category ->
                            val count = when (category) {
                                MilestoneCategory.MOTOR -> stats.motorTotal
                                MilestoneCategory.COGNITIVE -> stats.cognitiveTotal
                                MilestoneCategory.SPEECH -> stats.speechTotal
                            }
                            val achieved = when (category) {
                                MilestoneCategory.MOTOR -> stats.motorAchieved
                                MilestoneCategory.COGNITIVE -> stats.cognitiveAchieved
                                MilestoneCategory.SPEECH -> stats.speechAchieved
                            }

                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { viewModel.setCategoryFilter(category) },
                                label = { Text("${category.iconEmoji} ${category.displayName} ($achieved/$count)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = when (category) {
                                        MilestoneCategory.MOTOR -> AbleyTeal
                                        MilestoneCategory.COGNITIVE -> Color(0xFF4A7BD0)
                                        MilestoneCategory.SPEECH -> AbleyCoral
                                    },
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.White,
                                    labelColor = AbleyInk
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("category_filter_${category.id}")
                            )
                        }
                    }
                }
            }

            // 4. Status Filter Chips
            item(key = "status_filters_item") {
                Column {
                    Text(
                        text = "STATUS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AbleyInk.copy(alpha = 0.55f),
                            letterSpacing = 1.sp
                        ),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedStatus == null,
                            onClick = { viewModel.setStatusFilter(null) },
                            label = { Text("All Statuses") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AbleyInk,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = AbleyInk
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("status_filter_all")
                        )

                        MilestoneProgressStatus.entries.forEach { status ->
                            val count = when (status) {
                                MilestoneProgressStatus.ACHIEVED -> stats.achievedCount
                                MilestoneProgressStatus.IN_PROGRESS -> stats.inProgressCount
                                MilestoneProgressStatus.NOT_STARTED -> stats.notStartedCount
                            }

                            FilterChip(
                                selected = selectedStatus == status,
                                onClick = { viewModel.setStatusFilter(status) },
                                label = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(Color(status.colorHex))
                                        )
                                        Text("${status.displayName} ($count)")
                                    }
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(status.colorHex),
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.White,
                                    labelColor = AbleyInk
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("status_filter_${status.id}")
                            )
                        }
                    }
                }
            }

            // 5. Section Header with Tip
            item(key = "section_header_item") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MILESTONES (${filteredMilestones.size})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AbleyInk.copy(alpha = 0.55f),
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "Tap status pill to advance · Animated",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = AbleyTeal,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            // 6. Milestone Items with Animations (using Modifier.animateItem())
            if (filteredMilestones.isEmpty()) {
                item(key = "empty_milestones_state") {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .testTag("empty_milestones_card")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🌱", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No Milestones Match",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AbleyInk
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Try clearing filters, searching different keywords, or tap below to add a new milestone.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = AbleyInk.copy(alpha = 0.65f),
                                    lineHeight = 18.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.openAddDialog() },
                                colors = ButtonDefaults.buttonColors(containerColor = AbleyTeal),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("empty_add_milestone_button")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Add Milestone")
                            }
                        }
                    }
                }
            } else {
                items(
                    items = filteredMilestones,
                    key = { it.id }
                ) { milestone ->
                    AnimatedMilestoneCard(
                        milestone = milestone,
                        onStatusClick = { viewModel.cycleNextStatus(milestone) },
                        onEditClick = { viewModel.openEditMilestone(milestone) },
                        modifier = Modifier.animateItem(
                            fadeInSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            fadeOutSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            placementSpec = spring(
                                stiffness = Spring.StiffnessMediumLow,
                                dampingRatio = Spring.DampingRatioLowBouncy
                            )
                        )
                    )
                }
            }

            // Bottom spacing for FAB
            item(key = "bottom_fab_spacing") {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Modal Bottom Sheet to Add New Milestone (with Name, Category & Date Picker)
    if (isAddSheetOpen) {
        AddMilestoneBottomSheet(
            childName = childName,
            initialCategory = selectedCategory ?: MilestoneCategory.MOTOR,
            onDismiss = { viewModel.closeAddDialog() },
            onSave = { name, desc, cat, status, date, targetAge, notes ->
                viewModel.addMilestone(
                    title = name,
                    description = desc,
                    category = cat,
                    status = status,
                    date = date,
                    targetAgeMonths = targetAge,
                    notes = notes
                )
            }
        )
    }

    // Edit Milestone Dialog
    selectedMilestoneForEdit?.let { milestone ->
        EditMilestoneDialog(
            milestone = milestone,
            childName = childName,
            onDismiss = { viewModel.closeEditMilestone() },
            onSave = { title, desc, cat, status, date, targetAge, notes ->
                viewModel.updateMilestone(
                    milestone.copy(
                        title = title,
                        description = desc,
                        category = cat,
                        progressStatus = status,
                        date = date,
                        targetAgeMonths = targetAge,
                        notes = notes,
                        dateAchieved = if (status == MilestoneProgressStatus.ACHIEVED) date else null,
                        updatedTimestamp = System.currentTimeMillis()
                    )
                )
            },
            onDelete = { viewModel.deleteMilestone(milestone.id) }
        )
    }
}

/**
 * Milestone Card featuring reactive animations when status is toggled in the Room database:
 * - Animated background tint and border color on progress state changes
 * - Interactive tap pulse/bounce micro-interaction
 * - Animated icon transition between bullet and checkmark
 * - Celebratory expansion banner when milestone reaches ACHIEVED status
 */
@Composable
fun AnimatedMilestoneCard(
    milestone: ChildDevelopmentMilestone,
    onStatusClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var isPressedAnimation by remember { mutableStateOf(false) }

    // Category styling
    val categoryColor = when (milestone.category) {
        MilestoneCategory.MOTOR -> AbleyTeal
        MilestoneCategory.COGNITIVE -> Color(0xFF4A7BD0)
        MilestoneCategory.SPEECH -> AbleyCoral
    }

    // 1. Reactive color animations on status toggle
    val animatedStatusColor by animateColorAsState(
        targetValue = Color(milestone.progressStatus.colorHex),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "statusColor"
    )

    val animatedCardBg by animateColorAsState(
        targetValue = when (milestone.progressStatus) {
            MilestoneProgressStatus.ACHIEVED -> Color(0xFFF0FDF7)
            MilestoneProgressStatus.IN_PROGRESS -> Color(0xFFF6F9FF)
            MilestoneProgressStatus.NOT_STARTED -> Color.White
        },
        animationSpec = tween(durationMillis = 350),
        label = "cardBg"
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = when (milestone.progressStatus) {
            MilestoneProgressStatus.ACHIEVED -> AbleyTeal.copy(alpha = 0.4f)
            MilestoneProgressStatus.IN_PROGRESS -> Color(0xFF4A7BD0).copy(alpha = 0.3f)
            MilestoneProgressStatus.NOT_STARTED -> Color.Transparent
        },
        animationSpec = tween(durationMillis = 350),
        label = "cardBorder"
    )

    // 2. Interactive pill bounce animation
    val pillScale by animateFloatAsState(
        targetValue = if (isPressedAnimation) 1.15f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "pillScale"
    )

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = animatedCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(width = 1.5.dp, color = animatedBorderColor, shape = RoundedCornerShape(18.dp))
            .clickable { onEditClick() }
            .testTag("milestone_item_${milestone.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Category Badge & Interactive Animated Status Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Chip
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = categoryColor.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(milestone.category.iconEmoji, fontSize = 12.sp)
                        Text(
                            text = milestone.category.displayName.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = categoryColor,
                                letterSpacing = 0.8.sp,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                // Interactive Animated Status Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = animatedStatusColor.copy(alpha = 0.15f),
                    modifier = Modifier
                        .scale(pillScale)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            coroutineScope.launch {
                                isPressedAnimation = true
                                onStatusClick()
                                delay(180)
                                isPressedAnimation = false
                            }
                        }
                        .testTag("milestone_status_pill_${milestone.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        AnimatedContent(
                            targetState = milestone.progressStatus,
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(220, delayMillis = 50)) +
                                    scaleIn(initialScale = 0.7f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)))
                                    .togetherWith(fadeOut(animationSpec = tween(150)))
                            },
                            label = "statusIconAnim"
                        ) { status ->
                            if (status == MilestoneProgressStatus.ACHIEVED) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Achieved",
                                    tint = animatedStatusColor,
                                    modifier = Modifier.size(14.dp)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(animatedStatusColor)
                                    )
                            }
                        }

                        Text(
                            text = milestone.progressStatus.displayName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = animatedStatusColor,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = milestone.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = AbleyInk,
                    lineHeight = 22.sp
                )
            )

            if (milestone.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = milestone.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = AbleyInk.copy(alpha = 0.75f),
                        lineHeight = 18.sp
                    )
                )
            }

            // Observation Notes if present
            if (milestone.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AbleyIvory,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("📝", fontSize = 12.sp)
                        Text(
                            text = milestone.notes,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = AbleyInk.copy(alpha = 0.7f),
                                fontSize = 11.sp
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Celebratory Banner revealed when ACHIEVED
            AnimatedVisibility(
                visible = milestone.progressStatus == MilestoneProgressStatus.ACHIEVED,
                enter = expandVertically(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = AbleyTeal.copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("🎉", fontSize = 13.sp)
                            Text(
                                text = "Milestone Mastered & Logged to Journey",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AbleyTeal,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Footer: Date & Target Age
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (milestone.progressStatus == MilestoneProgressStatus.ACHIEVED && milestone.dateAchieved != null) {
                        "Achieved: ${milestone.dateAchieved}"
                    } else {
                        "Date: ${milestone.date}"
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = AbleyInk.copy(alpha = 0.55f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                )

                Text(
                    text = "Age: ~${milestone.targetAgeMonths / 12} yrs",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = AbleyInk.copy(alpha = 0.55f),
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

/**
 * Summary card for MilestonesScreen showing overall statistics and category progress.
 */
@Composable
fun MilestoneScreenSummaryCard(
    stats: MilestonesStats,
    childName: String,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("milestone_screen_summary_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "GROWTH PROGRESS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AbleyInk.copy(alpha = 0.5f),
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${stats.achievedCount} of ${stats.totalCount} Achieved",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = AbleyInk
                        )
                    )
                }

                Button(
                    onClick = onAddClick,
                    colors = ButtonDefaults.buttonColors(containerColor = AbleyTeal),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("summary_add_milestone_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Animated Progress Indicator
            LinearProgressIndicator(
                progress = { stats.percentageAchieved / 100f },
                color = AbleyTeal,
                trackColor = AbleySand.copy(alpha = 0.6f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 3-Domain Breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ScreenDomainPill(
                    icon = "🏃",
                    label = "Motor",
                    achieved = stats.motorAchieved,
                    total = stats.motorTotal,
                    color = AbleyTeal,
                    modifier = Modifier.weight(1f)
                )

                ScreenDomainPill(
                    icon = "🧩",
                    label = "Cognitive",
                    achieved = stats.cognitiveAchieved,
                    total = stats.cognitiveTotal,
                    color = Color(0xFF4A7BD0),
                    modifier = Modifier.weight(1f)
                )

                ScreenDomainPill(
                    icon = "💬",
                    label = "Speech",
                    achieved = stats.speechAchieved,
                    total = stats.speechTotal,
                    color = AbleyCoral,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ScreenDomainPill(
    icon: String,
    label: String,
    achieved: Int,
    total: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = AbleyIvory,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(icon, fontSize = 12.sp)
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AbleyInk.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$achieved/$total",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = color
                )
            )
        }
    }
}

/**
 * Edit dialog to modify or remove existing milestones.
 */
@Composable
fun EditMilestoneDialog(
    milestone: ChildDevelopmentMilestone,
    childName: String,
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        description: String,
        category: MilestoneCategory,
        status: MilestoneProgressStatus,
        date: String,
        targetAgeMonths: Int,
        notes: String
    ) -> Unit,
    onDelete: () -> Unit
) {
    var title by remember { mutableStateOf(milestone.title) }
    var description by remember { mutableStateOf(milestone.description) }
    var category by remember { mutableStateOf(milestone.category) }
    var status by remember { mutableStateOf(milestone.progressStatus) }
    var date by remember { mutableStateOf(milestone.date) }
    var targetAgeYears by remember { mutableIntStateOf((milestone.targetAgeMonths) / 12) }
    var notes by remember { mutableStateOf(milestone.notes) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("edit_milestone_dialog")
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
                    Text(
                        text = "Edit Milestone",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = AbleyInk
                        )
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = AbleyInk.copy(alpha = 0.6f))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Milestone Title *") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("edit_milestone_title")
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Criteria / Description") },
                            maxLines = 3,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        Text(
                            text = "Category *",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MilestoneCategory.entries.forEach { cat ->
                                val isSelected = category == cat
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) AbleyInk else AbleyIvory,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { category = cat }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(cat.iconEmoji, fontSize = 16.sp)
                                        Text(
                                            cat.displayName,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else AbleyInk,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Status *",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MilestoneProgressStatus.entries.forEach { st ->
                                val isSelected = status == st
                                val color = Color(st.colorHex)
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) color else AbleyIvory,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { status = st }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            st.displayName,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else AbleyInk,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = date,
                            onValueChange = { date = it },
                            label = { Text("Date *") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("Observation Notes") },
                            maxLines = 2,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = onDelete,
                                modifier = Modifier.testTag("delete_milestone_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFFD32F2F)
                                )
                            }

                            OutlinedButton(
                                onClick = onDismiss,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancel", color = AbleyInk)
                            }

                            Button(
                                onClick = {
                                    if (title.isNotBlank()) {
                                        onSave(title, description, category, status, date, targetAgeYears * 12, notes)
                                    }
                                },
                                enabled = title.isNotBlank(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AbleyTeal),
                                modifier = Modifier.weight(1f).testTag("save_edit_milestone_button")
                            ) {
                                Text("Update")
                            }
                        }
                    }
                }
            }
        }
    }
}
