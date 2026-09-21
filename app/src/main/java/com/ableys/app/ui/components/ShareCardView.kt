package com.ableys.app.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ableys.app.data.model.ShareCardData
import com.ableys.app.data.model.ShareCardTheme
import com.ableys.app.ui.theme.AbleyCoral
import com.ableys.app.ui.theme.AbleyInk
import com.ableys.app.ui.theme.AbleyIvory
import com.ableys.app.ui.theme.AbleySand
import com.ableys.app.ui.theme.AbleySurfaceDark
import com.ableys.app.ui.theme.AbleyTeal
import com.ableys.app.share.ShareCardRenderer
import androidx.compose.material3.Checkbox
import androidx.compose.foundation.clickable

@Composable
fun ShareCardDialog(
    initialData: ShareCardData,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentTheme by remember { mutableStateOf(initialData.theme) }
    var includeName by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = AbleyIvory,
            modifier = modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 24.dp)
                .testTag("share_card_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Abley's Share Card",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = AbleyInk
                            )
                        )
                        Text(
                            text = "Taste · Achievement · Identity · Pride",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = AbleyInk.copy(alpha = 0.6f)
                            )
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_share_card_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = AbleyInk)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Theme Selector Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = currentTheme == ShareCardTheme.DARK_STRAVA,
                        onClick = { currentTheme = ShareCardTheme.DARK_STRAVA },
                        label = { Text("Dark", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AbleySurfaceDark,
                            selectedLabelColor = Color.White
                        )
                    )
                    FilterChip(
                        selected = currentTheme == ShareCardTheme.CORAL_PRIDE,
                        onClick = { currentTheme = ShareCardTheme.CORAL_PRIDE },
                        label = { Text("Coral", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AbleyCoral,
                            selectedLabelColor = Color.White
                        )
                    )
                    FilterChip(
                        selected = currentTheme == ShareCardTheme.SAND_EDITORIAL,
                        onClick = { currentTheme = ShareCardTheme.SAND_EDITORIAL },
                        label = { Text("Sand", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AbleySand,
                            selectedLabelColor = AbleyInk
                        )
                    )
                    FilterChip(
                        selected = currentTheme == ShareCardTheme.TEAL_MOVEMENT,
                        onClick = { currentTheme = ShareCardTheme.TEAL_MOVEMENT },
                        label = { Text("Teal", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AbleyTeal,
                            selectedLabelColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // The Card itself
                ShareCardVisual(
                    data = initialData.copy(theme = currentTheme),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // A Status post reaches every contact in the phone. Whether a child's name goes
                // with it is a decision the parent makes here, each time, rather than one the
                // app makes quietly on their behalf.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { includeName = !includeName }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = includeName,
                        onCheckedChange = { includeName = it },
                        modifier = Modifier.testTag("include_child_name_toggle")
                    )
                    Column {
                        Text(
                            text = "Include ${initialData.childName}'s name",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            text = if (includeName) {
                                "The name will be on the image you send."
                            } else {
                                "The card will show the year only."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = AbleyInk.copy(alpha = 0.55f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Share Button triggering Android native intent
                Button(
                    onClick = {
                        shareCardContent(
                            context,
                            initialData.copy(theme = currentTheme),
                            includeChildName = includeName
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AbleyCoral),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("share_native_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Share Milestone",
                        color = Color.White,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ShareCardVisual(
    data: ShareCardData,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, accentColor) = when (data.theme) {
        ShareCardTheme.DARK_STRAVA -> Triple(Color(0xFF1B1918), Color.White, AbleyCoral)
        ShareCardTheme.CORAL_PRIDE -> Triple(AbleyCoral, Color.White, Color.White)
        ShareCardTheme.SAND_EDITORIAL -> Triple(Color(0xFFEFEAE0), AbleyInk, AbleyTeal)
        ShareCardTheme.TEAL_MOVEMENT -> Triple(AbleyTeal, Color.White, AbleyCoral)
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .testTag("share_card_visual")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Card Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "abley's",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = if (data.theme == ShareCardTheme.CORAL_PRIDE) Color.White else AbleyCoral,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp
                    )
                )
                Text(
                    text = data.ageOrYear.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = textColor.copy(alpha = 0.7f),
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = data.title.uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = textColor.copy(alpha = 0.75f),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Big Number Display
            Text(
                text = data.bigNumber,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 58.sp,
                    lineHeight = 62.sp,
                    fontWeight = FontWeight.Black,
                    color = if (data.theme == ShareCardTheme.DARK_STRAVA) AbleyCoral else textColor
                )
            )

            Text(
                text = data.unitLabel.uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = textColor.copy(alpha = 0.9f),
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Subtitle
            Text(
                text = data.statsSubtitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = textColor.copy(alpha = 0.85f),
                    lineHeight = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${data.childName} · ${data.ageOrYear}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = textColor.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                )

                Text(
                    text = data.footerMessage,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (data.theme == ShareCardTheme.CORAL_PRIDE) Color.White else AbleyCoral,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }
}

/**
 * Shares the card as a 1080x1920 image, with the text as a caption.
 *
 * Image first, because the destination is WhatsApp Status and a family group, where a picture is
 * the post and text is the caption under it. Falls back to text alone if the render or the write
 * fails, so a share never simply does nothing.
 *
 * [includeChildName] defaults to false. Status reaches every contact in the phone; putting a
 * child's name in front of all of them should be a parent's decision, not a default.
 */
private fun shareCardContent(
    context: Context,
    data: ShareCardData,
    includeChildName: Boolean = false
) {
    val caption = buildString {
        appendLine("Abley\u2019s \u00b7 ${data.title}")
        appendLine("${data.bigNumber} ${data.unitLabel}")
        appendLine(data.statsSubtitle)
        if (includeChildName) appendLine("${data.childName} \u00b7 ${data.ageOrYear}")
        appendLine()
        appendLine("Learn together. Move together. Remember it forever.")
        append("https://ableys.in")
    }

    val uri = runCatching {
        ShareCardRenderer.writeToCache(
            context,
            ShareCardRenderer.render(data, includeChildName)
        )
    }.getOrNull()

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, caption)
        if (uri != null) {
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            type = "text/plain"
        }
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share with family")
    if (uri != null) shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(shareIntent)
}
