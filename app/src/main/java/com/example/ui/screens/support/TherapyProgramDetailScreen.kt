package com.example.ui.screens.support

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EquipmentProduct
import com.example.data.model.TherapyProgram
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleyCoralLight
import com.example.ui.theme.AbleyInk
import com.example.ui.theme.AbleyIvory
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal

@Composable
fun TherapyProgramDetailScreen(
    program: TherapyProgram,
    equipmentList: List<EquipmentProduct>,
    onBack: () -> Unit,
    onStartSession: () -> Unit,
    onShowSafetyGuidance: (String) -> Unit,
    onOpenShopItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val equipmentItem = program.equipmentSku?.let { sku ->
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
                        modifier = Modifier.testTag("back_button_therapy_detail")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AbleyInk
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AbleyTeal.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = program.area.displayName.uppercase(),
                            color = AbleyTeal,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "Therapy at Home",
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
                        onClick = onStartSession,
                        colors = ButtonDefaults.buttonColors(containerColor = AbleyTeal),
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("start_therapy_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "START HOME THERAPY SESSION",
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        )
                    }
                }
            }
        },
        modifier = modifier.testTag("therapy_detail_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title & Subtitle (Page 13 & 14)
            item {
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    Text(
                        text = program.title,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = AbleyInk,
                            fontSize = 28.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${program.durationMinutes} min · ${program.clinicalTarget}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = AbleyInk.copy(alpha = 0.65f),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            // Clinical Rigor & OT Review Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(AbleyTeal.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = AbleyTeal,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Clinical Rigor · Warm Delivery",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AbleyInk
                                )
                            )
                            Text(
                                text = "Designed for developmental delays, sensory processing and motor planning.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = AbleyInk.copy(alpha = 0.65f),
                                    lineHeight = 16.sp
                                )
                            )
                        }
                    }
                }
            }

            // Safety Guidance Card (Page 14)
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = AbleyCoralLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HealthAndSafety,
                                contentDescription = null,
                                tint = AbleyCoral,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Safety Guidance Before You Start",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AbleyCoral
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = program.safetyGuidance,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = AbleyInk.copy(alpha = 0.85f),
                                lineHeight = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = { onShowSafetyGuidance(program.safetyGuidance) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AbleyCoral),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.testTag("open_safety_guidance_button")
                        ) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.size(6.dp))
                            Text("Review Full Home Safety Boundaries", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Equipment Callout (Page 14 & 16)
            item {
                Column {
                    Text(
                        text = "EQUIPMENT REQUIRED",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AbleyInk.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    val equipName = program.equipmentName
                    if (equipName != null) {
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
                                    Icon(
                                        imageVector = Icons.Default.ShoppingBag,
                                        contentDescription = "Equipment",
                                        tint = AbleyTeal,
                                        modifier = Modifier.size(22.dp)
                                    )

                                    Column {
                                        Text(
                                            text = equipName,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = AbleyInk
                                            )
                                        )
                                        Text(
                                            text = if (hasEquipment) "✓ In your home kit" else "Available from Abley's",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = if (hasEquipment) AbleyTeal else AbleyCoral,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }

                                if (program.equipmentSku.isNotBlank()) {
                                    Text(
                                        text = if (hasEquipment) "View Details" else "View $equipName →",
                                        color = AbleyTeal,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .clickable { onOpenShopItem(program.equipmentSku) }
                                            .testTag("shop_therapy_equipment_link")
                                    )
                                }
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "✓ No specialized equipment needed for this session.",
                                color = AbleyTeal,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }

            // Session Flow Steps
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "SESSION PROTOCOL (${program.steps.size} STEPS)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AbleyInk.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )

                    program.steps.forEachIndexed { index, step ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = AbleySand,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${index + 1}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = AbleyInk
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = step.name,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            color = AbleyInk,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = step.instruction,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = AbleyInk.copy(alpha = 0.8f),
                                            lineHeight = 18.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}
