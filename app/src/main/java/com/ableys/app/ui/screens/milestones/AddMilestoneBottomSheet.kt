package com.ableys.app.ui.screens.milestones

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ableys.app.data.model.MilestoneCategory
import com.ableys.app.data.model.MilestoneProgressStatus
import com.ableys.app.ui.theme.AbleyInk
import com.ableys.app.ui.theme.AbleyIvory
import com.ableys.app.ui.theme.AbleySand
import com.ableys.app.ui.theme.AbleyTeal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Modal Bottom Sheet designed to add a new child development milestone to the Room database.
 * Includes text fields for the milestone name and category, and a Material 3 DatePicker.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMilestoneBottomSheet(
    childName: String = "Aarav",
    initialCategory: MilestoneCategory = MilestoneCategory.MOTOR,
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        description: String,
        category: MilestoneCategory,
        status: MilestoneProgressStatus,
        date: String,
        targetAgeMonths: Int,
        notes: String
    ) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var name by remember { mutableStateOf("") }
    var categoryText by remember { mutableStateOf(initialCategory.displayName) }
    var selectedCategoryEnum by remember { mutableStateOf(initialCategory) }
    var description by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf(MilestoneProgressStatus.IN_PROGRESS) }

    // Date state
    val defaultDateFormatted = remember {
        SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())
    }
    var selectedDateFormatted by remember { mutableStateOf(defaultDateFormatted) }
    var showDatePickerDialog by remember { mutableStateOf(false) }

    // Target age in years
    var targetAgeYears by remember { mutableIntStateOf(5) }
    var notes by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }

    // Material 3 DatePicker state
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AbleyIvory,
        dragHandle = null,
        modifier = modifier.testTag("add_milestone_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Add New Milestone",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = AbleyInk,
                            letterSpacing = (-0.5).sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Track developmental achievements & goals for $childName",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = AbleyInk.copy(alpha = 0.65f)
                        )
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_add_milestone_sheet")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close sheet",
                        tint = AbleyInk.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 1. Milestone Name Text Field
            Text(
                text = "MILESTONE NAME *",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AbleyInk.copy(alpha = 0.65f),
                    letterSpacing = 0.8.sp
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    if (it.isNotBlank()) nameError = false
                },
                placeholder = {
                    Text(
                        "e.g. Balances on one foot for 10s",
                        style = MaterialTheme.typography.bodyMedium.copy(color = AbleyInk.copy(alpha = 0.4f))
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = AbleyTeal
                    )
                },
                trailingIcon = {
                    if (name.isNotBlank()) {
                        IconButton(onClick = { name = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear name",
                                tint = AbleyInk.copy(alpha = 0.4f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                isError = nameError,
                supportingText = if (nameError) {
                    { Text("Milestone name is required", color = MaterialTheme.colorScheme.error) }
                } else null,
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = AbleyTeal,
                    unfocusedBorderColor = AbleySand
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("milestone_name_input")
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Category Text Field and Quick Preset Chips
            Text(
                text = "CATEGORY *",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AbleyInk.copy(alpha = 0.65f),
                    letterSpacing = 0.8.sp
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = categoryText,
                onValueChange = { newText ->
                    categoryText = newText
                    // Map to enum if matching
                    val matchedEnum = MilestoneCategory.entries.firstOrNull {
                        it.displayName.equals(newText.trim(), ignoreCase = true)
                    }
                    if (matchedEnum != null) {
                        selectedCategoryEnum = matchedEnum
                    }
                },
                placeholder = { Text("e.g. Motor, Cognitive, Speech") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = null,
                        tint = AbleyTeal
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = AbleyTeal,
                    unfocusedBorderColor = AbleySand
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("milestone_category_input")
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick category preset chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MilestoneCategory.entries.forEach { cat ->
                    val isSelected = selectedCategoryEnum == cat && categoryText.equals(cat.displayName, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedCategoryEnum = cat
                            categoryText = cat.displayName
                        },
                        label = { Text("${cat.iconEmoji} ${cat.displayName}") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AbleyTeal,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = AbleyInk
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("chip_preset_${cat.id}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Date Picker Field
            Text(
                text = "DATE (ACHIEVED / TARGET) *",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AbleyInk.copy(alpha = 0.65f),
                    letterSpacing = 0.8.sp
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = selectedDateFormatted,
                onValueChange = { selectedDateFormatted = it },
                readOnly = true,
                placeholder = { Text("Select date") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date icon",
                        tint = AbleyTeal
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { showDatePickerDialog = true },
                        modifier = Modifier.testTag("milestone_date_picker_button")
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AbleyTeal.copy(alpha = 0.12f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Open Date Picker",
                                tint = AbleyTeal,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = AbleyTeal,
                    unfocusedBorderColor = AbleySand
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { showDatePickerDialog = true }
                    .testTag("milestone_date_input")
            )

            // Date Quick Suggestions: Today, Yesterday
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    modifier = Modifier
                        .clickable {
                            selectedDateFormatted = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())
                        }
                ) {
                    Text(
                        text = "📅 Today",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = AbleyTeal
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    modifier = Modifier
                        .clickable {
                            val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
                            selectedDateFormatted = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(cal.time)
                        }
                ) {
                    Text(
                        text = "⏮️ Yesterday",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = AbleyInk.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Progress Status Selector
            Text(
                text = "INITIAL STATUS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AbleyInk.copy(alpha = 0.65f),
                    letterSpacing = 0.8.sp
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MilestoneProgressStatus.entries.forEach { status ->
                    val isSelected = selectedStatus == status
                    val statusColor = Color(status.colorHex)

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) statusColor else Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedStatus = status }
                            .testTag("status_select_${status.id}")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = status.displayName,
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

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Description / Criteria (Optional)
            Text(
                text = "CRITERIA & DESCRIPTION (OPTIONAL)",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AbleyInk.copy(alpha = 0.65f),
                    letterSpacing = 0.8.sp
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = {
                    Text(
                        "Describe what skill or movement the child demonstrates...",
                        style = MaterialTheme.typography.bodySmall.copy(color = AbleyInk.copy(alpha = 0.4f))
                    )
                },
                maxLines = 3,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = AbleyTeal,
                    unfocusedBorderColor = AbleySand
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("milestone_desc_input")
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Observation Notes (Optional)
            Text(
                text = "PARENT / THERAPIST OBSERVATIONS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AbleyInk.copy(alpha = 0.65f),
                    letterSpacing = 0.8.sp
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                placeholder = {
                    Text(
                        "e.g. Mastered during evening playground therapy session...",
                        style = MaterialTheme.typography.bodySmall.copy(color = AbleyInk.copy(alpha = 0.4f))
                    )
                },
                maxLines = 2,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = AbleyTeal,
                    unfocusedBorderColor = AbleySand
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("milestone_notes_input")
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons: Cancel & Add Milestone
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("cancel_add_milestone_button")
                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = AbleyInk
                        )
                    )
                }

                Button(
                    onClick = {
                        if (name.isBlank()) {
                            nameError = true
                        } else {
                            // Resolve category enum
                            val finalCategory = MilestoneCategory.entries.firstOrNull {
                                it.displayName.equals(categoryText.trim(), ignoreCase = true)
                            } ?: selectedCategoryEnum

                            onSave(
                                name.trim(),
                                description.trim(),
                                finalCategory,
                                selectedStatus,
                                selectedDateFormatted.trim(),
                                targetAgeYears * 12,
                                notes.trim()
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AbleyTeal),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1.5f)
                        .height(50.dp)
                        .testTag("save_new_milestone_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Add Milestone",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Material 3 DatePickerDialog
    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { utcMillis ->
                            val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).apply {
                                timeZone = TimeZone.getTimeZone("UTC")
                            }
                            selectedDateFormatted = formatter.format(Date(utcMillis))
                        }
                        showDatePickerDialog = false
                    },
                    modifier = Modifier.testTag("date_picker_confirm_button")
                ) {
                    Text(
                        text = "OK",
                        fontWeight = FontWeight.Bold,
                        color = AbleyTeal
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePickerDialog = false },
                    modifier = Modifier.testTag("date_picker_cancel_button")
                ) {
                    Text(
                        text = "Cancel",
                        color = AbleyInk.copy(alpha = 0.7f)
                    )
                }
            },
            modifier = Modifier.testTag("milestone_date_picker_dialog")
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = AbleyTeal,
                    todayDateBorderColor = AbleyTeal,
                    selectedDayContentColor = Color.White
                )
            )
        }
    }
}
