package com.example.ui.screens.story

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.model.MemoryItem
import com.example.data.model.MemorySource
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal
import java.util.Calendar
import java.util.Locale

/**
 * A calendar period with the memories that fall inside it.
 *
 * Grouping is done on [MemoryItem.timestamp] rather than the display date string, because the
 * string is parent-entered text and a timeline that silently mis-sorts is worse than one that
 * shows an awkward date.
 */
data class MemoryPeriod(
    val year: Int,
    val month: Int?,          // null for a whole-year period
    val label: String,
    val memories: List<MemoryItem>
) {
    val photoCount: Int get() = memories.count { it.photoUri != null }
    val parentCount: Int get() = memories.count { it.source == MemorySource.PARENT }
    val autoCount: Int get() = memories.count { it.source == MemorySource.ABLEY_AUTO }
    val coverPhoto: String? get() = memories.firstOrNull { it.photoUri != null }?.photoUri
}

private val MONTH_NAMES = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

/** Groups memories into months, newest first. */
fun groupIntoMonths(memories: List<MemoryItem>): List<MemoryPeriod> {
    val cal = Calendar.getInstance()
    return memories
        .groupBy {
            cal.timeInMillis = it.timestamp
            cal.get(Calendar.YEAR) to cal.get(Calendar.MONTH)
        }
        .map { (key, items) ->
            val (year, month) = key
            MemoryPeriod(
                year = year,
                month = month,
                label = "${MONTH_NAMES[month]} $year",
                memories = items.sortedByDescending { it.timestamp }
            )
        }
        .sortedWith(compareByDescending<MemoryPeriod> { it.year }.thenByDescending { it.month ?: 0 })
}

/** Groups memories into years, newest first. */
fun groupIntoYears(memories: List<MemoryItem>): List<MemoryPeriod> {
    val cal = Calendar.getInstance()
    return memories
        .groupBy {
            cal.timeInMillis = it.timestamp
            cal.get(Calendar.YEAR)
        }
        .map { (year, items) ->
            MemoryPeriod(
                year = year,
                month = null,
                label = year.toString(),
                memories = items.sortedByDescending { it.timestamp }
            )
        }
        .sortedByDescending { it.year }
}

/**
 * Month view. One card per month showing what the family actually captured, with the share
 * action the spec puts on the monthly card.
 */
@Composable
fun MemoryMonthView(
    periods: List<MemoryPeriod>,
    onSharePeriod: (MemoryPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().testTag("memory_month_view"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (periods.isEmpty()) {
            EmptyPeriodState(
                headline = "No months yet",
                body = "Once you capture a few moments they will gather here, a month at a time."
            )
        }
        periods.forEach { period ->
            PeriodCard(
                period = period,
                accent = AbleyCoral,
                statLine = "${period.memories.size} moments · ${period.photoCount} photos",
                onShare = { onSharePeriod(period) }
            )
        }
    }
}

/**
 * Year view. The rollup the Year in Growing story is built from, kept as a browsable surface so
 * the annual story is not the only way to see a whole year.
 */
@Composable
fun MemoryYearView(
    periods: List<MemoryPeriod>,
    onSharePeriod: (MemoryPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().testTag("memory_year_view"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (periods.isEmpty()) {
            EmptyPeriodState(
                headline = "No years yet",
                body = "A year of growing starts with one moment. Capture something today."
            )
        }
        periods.forEach { period ->
            PeriodCard(
                period = period,
                accent = AbleyTeal,
                statLine = "${period.memories.size} moments · " +
                    "${period.parentCount} yours · ${period.autoCount} added by Abley's",
                onShare = { onSharePeriod(period) }
            )
        }
    }
}

@Composable
private fun PeriodCard(
    period: MemoryPeriod,
    accent: Color,
    statLine: String,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, AbleySand, RoundedCornerShape(20.dp))
            .testTag("memory_period_${period.year}_${period.month ?: "all"}")
    ) {
        val cover = period.coverPhoto
        if (cover != null) {
            AsyncImage(
                model = cover,
                contentDescription = "${period.label} cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = period.label.uppercase(Locale.getDefault()),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = accent
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${period.memories.size}",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = statLine,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black.copy(alpha = 0.55f)
                )
            }
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(21.dp))
                    .background(accent.copy(alpha = 0.12f))
                    .clickable { onShare() }
                    .testTag("share_period_${period.year}_${period.month ?: "all"}"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.IosShare,
                    contentDescription = "Share ${period.label}",
                    tint = accent,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyPeriodState(
    headline: String,
    body: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AbleySand.copy(alpha = 0.4f))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = headline,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = body,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black.copy(alpha = 0.6f)
        )
    }
}
