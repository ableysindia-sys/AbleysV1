package com.example.ui.screens.support

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TherapyProgram
import com.example.ui.theme.AbleyInk
import com.example.ui.theme.AbleyIvory
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal

private val SafetyRed = Color(0xFFC0392B)
private val SafetyGreen = Color(0xFF1F7A4C)

/**
 * Safety guidance as two pictograms and a tap, shown before a session can start.
 *
 * A paragraph of liability text does one job well, which is protecting whoever wrote it. It does
 * not reach a grandmother who reads little English, and in this app the person running the
 * session is often not the person who installed it. So the boundary is carried by a green tick
 * and a red cross, and the written guidance sits underneath for whoever wants it.
 *
 * The acknowledgment is a real gate rather than a checkbox by the button: Start does not exist
 * until the thumbs-up is pressed. That is deliberate friction, and it is the only friction in
 * this app I would defend, because the alternative is a child on a swing with nobody having read
 * anything.
 *
 * The do and don't lines are derived from the programme's own safety text. Illustrated postures
 * are the better version of this and need an illustrator; the structure here takes them without
 * changing.
 */
@Composable
fun SafetyGate(
    program: TherapyProgram,
    acknowledged: Boolean,
    onAcknowledge: () -> Unit,
    modifier: Modifier = Modifier
) {
    val doLine = SafetyLines.doFor(program)
    val dontLine = SafetyLines.dontFor(program)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AbleyIvory)
            .border(1.dp, AbleySand, RoundedCornerShape(20.dp))
            .padding(18.dp)
            .testTag("safety_gate")
    ) {
        Text(
            text = "BEFORE YOU START",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            letterSpacing = 1.4.sp,
            color = AbleyInk.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(14.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SafetyPanel(
                icon = Icons.Filled.Check,
                accent = SafetyGreen,
                label = "DO",
                body = doLine,
                modifier = Modifier.weight(1f)
            )
            SafetyPanel(
                icon = Icons.Filled.Close,
                accent = SafetyRed,
                label = "DON'T",
                body = dontLine,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = program.safetyGuidance,
            style = MaterialTheme.typography.bodySmall,
            color = AbleyInk.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(18.dp))

        val scale by animateFloatAsState(
            targetValue = if (acknowledged) 1f else 0.94f,
            animationSpec = tween(220),
            label = "ack"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(if (acknowledged) AbleyTeal else AbleySand.copy(alpha = 0.55f))
                .clickable(enabled = !acknowledged) { onAcknowledge() }
                .testTag("safety_acknowledge"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size((44 * scale).dp)
                    .clip(CircleShape)
                    .background(if (acknowledged) Color.White else Color.White.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ThumbUp,
                    contentDescription = if (acknowledged) "Understood" else "Tap to confirm you have read this",
                    tint = if (acknowledged) AbleyTeal else AbleyInk.copy(alpha = 0.5f),
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.size(14.dp))
            Text(
                text = if (acknowledged) "Understood" else "Tap to confirm",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = if (acknowledged) Color.White else AbleyInk.copy(alpha = 0.65f)
            )
        }
    }
}

@Composable
private fun SafetyPanel(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accent: Color,
    label: String,
    body: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(accent.copy(alpha = 0.08f))
            .border(2.dp, accent.copy(alpha = 0.55f), RoundedCornerShape(16.dp))
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(accent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = accent
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = body,
            style = MaterialTheme.typography.bodySmall,
            color = AbleyInk.copy(alpha = 0.8f)
        )
    }
}
