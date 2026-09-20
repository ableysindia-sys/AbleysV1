package com.example.ui.screens.move

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EquipmentProduct
import com.example.data.model.MoveActivity
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleyCoralLight
import com.example.ui.theme.AbleyInk
import com.example.ui.theme.AbleyIvory
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.VerifiedUser
import com.example.data.model.ReviewState
import com.example.ui.components.HouseholdAlternativeCard

@Composable
fun ActivityDetailScreen(
    activity: MoveActivity,
    equipmentList: List<EquipmentProduct>,
    onBack: () -> Unit,
    onStartActivity: () -> Unit,
    onOpenShopItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val equipmentItem = activity.equipmentSku?.let { sku ->
        equipmentList.firstOrNull { it.sku == sku }
    }
    val hasEquipment = equipmentItem?.isOwned ?: false

    Scaffold(
        containerColor = AbleyIvory,
        topBar = {
            Surface(
                color = AbleyIvory,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("back_button_move_detail")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AbleyInk
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AbleyCoralLight
                    ) {
                        Text(
                            text = activity.categoryBadge,
                            color = AbleyCoral,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "Move",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = AbleyInk.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = onStartActivity,
                        colors = ButtonDefaults.buttonColors(containerColor = AbleyInk),
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("start_activity_button")
                    ) {
                        Text(
                            text = "START ACTIVITY",
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }
            }
        },
        modifier = modifier.testTag("activity_detail_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Title & Subtitle (Page 10)
            item {
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    Text(
                        text = activity.title,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = AbleyInk,
                            fontSize = 28.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${activity.durationMinutes} min · ${activity.targetArea} · ${activity.motorType}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = AbleyInk.copy(alpha = 0.65f),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            // 1. Demonstration Container (Page 10, #1)
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .clickable { onStartActivity() }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF2C2725),
                                        Color(0xFF1B1817)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Triangle Play Icon / Demonstration Badge
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Watch Demo",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "1  Demonstration",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Text(
                                text = "Animated video showing exactly what to do",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            )
                        }
                    }
                }
            }

            // Target Tags
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    activity.targetTags.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = AbleySand
                        ) {
                            Text(
                                text = tag,
                                color = AbleyInk,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            // Description
            item {
                Text(
                    text = activity.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = AbleyInk.copy(alpha = 0.85f),
                        lineHeight = 22.sp
                    )
                )
            }

            // Who has signed this off, stated plainly. An app that sells equipment alongside
            // its activities has to be exact about which of them a practitioner has actually
            // read, or every claim on every screen is worth less.
            item { ReviewStatusRow(activity = activity) }

            // 2 & 3. Equipment Needed & Contextual Shop Link (Page 10, #2 & #3)
            item {
                Column {
                    Text(
                        text = "EQUIPMENT NEEDED",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AbleyInk.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (activity.equipmentName != null) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    if (hasEquipment) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "In Kit",
                                            tint = AbleyTeal,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.ShoppingBag,
                                            contentDescription = "Equipment",
                                            tint = AbleyCoral,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = activity.equipmentName,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = AbleyInk
                                            )
                                        )
                                        Text(
                                            text = if (hasEquipment) "In your home kit" else "Available from Abley's",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = if (hasEquipment) AbleyTeal else AbleyCoral,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }

                                if (activity.equipmentSku != null) {
                                    Text(
                                        text = if (hasEquipment) "View" else "Don't have them? Shop",
                                        color = if (hasEquipment) AbleyInk.copy(alpha = 0.6f) else AbleyCoral,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .clickable { onOpenShopItem(activity.equipmentSku) }
                                            .testTag("shop_equipment_link")
                                    )
                                }
                            }
                        }

                        activity.householdAlternative?.let { alternative ->
                            Spacer(modifier = Modifier.height(10.dp))
                            HouseholdAlternativeCard(alternative = alternative)
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "✓ None needed · Just an open safe floor space!",
                                color = AbleyTeal,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }

            // Exercise Steps Breakdown
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "EXERCISE PACING",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AbleyInk.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )

                    activity.demonstrationSteps.forEachIndexed { index, step ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = AbleyInk,
                                    lineHeight = 18.sp
                                ),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

/** States the review position for one activity: approved by whom, or not yet reviewed. */
@Composable
private fun ReviewStatusRow(
    activity: MoveActivity,
    modifier: Modifier = Modifier
) {
    val approved = activity.reviewState == ReviewState.APPROVED
    val accent = if (approved) AbleyTeal else AbleyInk.copy(alpha = 0.45f)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AbleySand.copy(alpha = 0.4f))
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .testTag("review_status_${activity.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (approved) Icons.Filled.VerifiedUser else Icons.Filled.Schedule,
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = if (approved) {
                    "Reviewed by a ${activity.reviewDiscipline.displayName.lowercase()}"
                } else {
                    "Awaiting review by a ${activity.reviewDiscipline.displayName.lowercase()}"
                },
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = AbleyInk.copy(alpha = 0.8f)
            )
            if (!approved) {
                Text(
                    text = "Use your own judgement and stop if your child is not enjoying it.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AbleyInk.copy(alpha = 0.55f)
                )
            }
        }
    }
}
