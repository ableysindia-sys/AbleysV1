package com.ableys.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ableys.app.ui.theme.AbleyCoral
import com.ableys.app.ui.theme.AbleyInk
import com.ableys.app.ui.theme.AbleyIvory
import com.ableys.app.ui.theme.AbleyTeal

/**
 * Asked once, the first time an activity actually needs a piece of equipment.
 *
 * The alternative shape -- an inventory checklist during onboarding -- puts the whole product
 * catalogue in front of a parent before they have seen a single activity, which reads as a
 * shopping list standing between them and the app. Asking at the moment the answer matters costs
 * one tap and gives a real answer, and the answer is cached so it is never asked twice.
 *
 * "Not yet" is not a dead end and is deliberately not a sales moment: it takes the family
 * straight to the household version, which every gated activity is required to have.
 */
@Composable
fun EquipmentCheckSheet(
    equipmentName: String,
    householdAlternative: String?,
    onAnswer: (owned: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = AbleyIvory,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .testTag("equipment_check_sheet")
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Do you have a $equipmentName at home?",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AbleyInk
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "We'll only ask once, and set up every session to match.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = AbleyInk.copy(alpha = 0.7f)
                    ),
                    textAlign = TextAlign.Center
                )

                AnswerButton(
                    label = "Yes, we have one",
                    background = AbleyTeal,
                    textColor = Color.White,
                    testTag = "equipment_check_yes",
                    onClick = { onAnswer(true) }
                )
                AnswerButton(
                    label = if (householdAlternative != null) {
                        "Not yet — show the home version"
                    } else {
                        "Not yet"
                    },
                    background = Color.White,
                    textColor = AbleyCoral,
                    testTag = "equipment_check_no",
                    onClick = { onAnswer(false) }
                )
            }
        }
    }
}

@Composable
private fun AnswerButton(
    label: String,
    background: Color,
    textColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = background,
        modifier = Modifier
            .fillMaxWidth()
            // Generously tall: often answered one-handed while a child is already moving.
            .heightIn(min = 64.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, textColor.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
            .background(background)
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}
