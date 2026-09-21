package com.ableys.app.ui.screens.move

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ableys.app.data.model.MoveActivity
import com.ableys.app.data.model.MoveProgram
import com.ableys.app.ui.theme.AbleyIvory
import com.ableys.app.ui.theme.AbleySand

/**
 * The spec's "Program or challenge" screen.
 *
 * A programme is a running order over the reviewed activity catalogue, shown as a ladder of days.
 * The next unfinished day is the one the screen points at -- families miss days, and a programme
 * that punishes a gap gets abandoned rather than resumed.
 */
@Composable
fun MoveProgramDetailScreen(
    program: MoveProgram,
    activityForId: (String) -> MoveActivity?,
    completedDays: Set<Int>,
    onBack: () -> Unit,
    onStartDay: (dayNumber: Int, activity: MoveActivity) -> Unit,
    onResetProgram: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = Color(program.accentColorHex)
    val doneCount = completedDays.size
    val fraction = if (program.totalDays == 0) 0f else doneCount.toFloat() / program.totalDays
    val nextDay = (1..program.totalDays).firstOrNull { it !in completedDays }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AbleyIvory)
            .testTag("move_program_detail_${program.id}"),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 32.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("program_back_button")
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back to Move")
                    }
                    Text(
                        text = program.format.displayName.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = accent
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = program.title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = program.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = program.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black.copy(alpha = 0.75f)
                )

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "$doneCount of ${program.totalDays} days",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { fraction },
                    color = accent,
                    trackColor = AbleySand,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .testTag("program_progress_${program.id}")
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        items(program.days, key = { it.dayNumber }) { day ->
            val activity = activityForId(day.activityId)
            ProgramDayRow(
                dayNumber = day.dayNumber,
                focusLabel = day.focusLabel,
                activity = activity,
                isComplete = day.dayNumber in completedDays,
                isNext = day.dayNumber == nextDay,
                accent = accent,
                onStart = { if (activity != null) onStartDay(day.dayNumber, activity) }
            )
        }

        if (doneCount > 0) {
            item {
                TextButton(
                    onClick = onResetProgram,
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .testTag("program_reset_button")
                ) {
                    Text("Start this challenge again", color = Color.Black.copy(alpha = 0.55f))
                }
            }
        }
    }
}

@Composable
private fun ProgramDayRow(
    dayNumber: Int,
    focusLabel: String,
    activity: MoveActivity?,
    isComplete: Boolean,
    isNext: Boolean,
    accent: Color,
    onStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isNext) Color.White else Color.White.copy(alpha = 0.6f))
            .border(
                width = if (isNext) 2.dp else 1.dp,
                color = if (isNext) accent else AbleySand,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(enabled = activity != null) { onStart() }
            .padding(14.dp)
            .testTag("program_day_$dayNumber"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(if (isComplete) accent else AbleySand),
            contentAlignment = Alignment.Center
        ) {
            if (isComplete) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "Day $dayNumber complete",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = "$dayNumber",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = activity?.title ?: "Activity unavailable",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (activity != null) {
                    "$focusLabel · ${activity.durationMinutes} min"
                } else {
                    focusLabel
                },
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black.copy(alpha = 0.55f)
            )
        }

        if (!isComplete && activity != null) {
            Icon(
                Icons.Filled.PlayArrow,
                contentDescription = "Start day $dayNumber",
                tint = if (isNext) accent else Color.Black.copy(alpha = 0.35f)
            )
        }
    }
}
