package com.example.ui.screens.grow

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.SkillArea
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleyCoralLight
import com.example.ui.theme.AbleyGold
import com.example.ui.theme.AbleyInk
import com.example.ui.theme.AbleyIvory
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal

@Composable
fun InteractiveGameDialog(
    skillArea: SkillArea,
    onDismiss: () -> Unit,
    onCompleteGame: (xpGain: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var gameCompleted by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableIntStateOf(0) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = AbleyIvory,
            modifier = modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 24.dp)
                .testTag("interactive_game_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AbleyCoralLight
                    ) {
                        Text(
                            text = "${skillArea.displayName.uppercase()} · QUEST",
                            color = AbleyCoral,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_game_dialog")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = AbleyInk)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!gameCompleted) {
                    when (skillArea) {
                        SkillArea.NUMBERS -> NumbersMiniGame(
                            onSuccess = {
                                selectedOption = it
                                gameCompleted = true
                            }
                        )
                        SkillArea.LITERACY -> LiteracyMiniGame(
                            onSuccess = {
                                selectedOption = it
                                gameCompleted = true
                            }
                        )
                        SkillArea.EMOTIONS -> EmotionsMiniGame(
                            onSuccess = {
                                selectedOption = it
                                gameCompleted = true
                            }
                        )
                        SkillArea.COMMUNICATION -> CommunicationMiniGame(
                            onSuccess = {
                                selectedOption = it
                                gameCompleted = true
                            }
                        )
                        SkillArea.THINKING -> ThinkingMiniGame(
                            onSuccess = {
                                selectedOption = it
                                gameCompleted = true
                            }
                        )
                        SkillArea.CREATIVITY -> CreativityMiniGame(
                            onSuccess = {
                                selectedOption = it
                                gameCompleted = true
                            }
                        )
                        SkillArea.EVERYDAY_SKILLS -> EverydaySkillsMiniGame(
                            onSuccess = {
                                selectedOption = it
                                gameCompleted = true
                            }
                        )
                    }
                } else {
                    // Celebration & XP Reward View
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(AbleyGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = AbleyGold,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Level Quest Complete!",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AbleyInk
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Mastery logged for ${skillArea.displayName}.\nYou unlocked +25 XP and progressed towards the next badge!",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = AbleyInk.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { onCompleteGame(25) },
                            colors = ButtonDefaults.buttonColors(containerColor = AbleyCoral),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("claim_xp_button")
                        ) {
                            Text(
                                text = "Claim +25 XP & Continue",
                                color = Color.White,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NumbersMiniGame(onSuccess: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Stepping Stone Math",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = AbleyInk)
        )
        Text(
            text = "Aarav is on stone 4. If he leaps 2 stones forward, which stone does he land on?",
            style = MaterialTheme.typography.bodyMedium.copy(color = AbleyInk.copy(alpha = 0.8f), textAlign = TextAlign.Center),
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("4", "5", "6", "7").forEach { num ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (num == "4") AbleyCoral else AbleySand,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = num,
                            fontWeight = FontWeight.Bold,
                            color = if (num == "4") Color.White else AbleyInk
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("Stone 5", "Stone 6", "Stone 8").forEach { option ->
                Button(
                    onClick = {
                        if (option == "Stone 6") onSuccess(option)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("option_$option")
                ) {
                    Text(text = option, color = AbleyInk, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun LiteracyMiniGame(onSuccess: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Word Rhyme Safari",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = AbleyInk)
        )
        Text(
            text = "Which word rhymes with 'CAT' and keeps your head warm in the winter?",
            style = MaterialTheme.typography.bodyMedium.copy(color = AbleyInk.copy(alpha = 0.8f), textAlign = TextAlign.Center),
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("DOG", "HAT", "SUN").forEach { word ->
                Button(
                    onClick = {
                        if (word == "HAT") onSuccess(word)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("word_$word")
                ) {
                    Text(text = word, color = AbleyInk, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun EmotionsMiniGame(onSuccess: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Feelings & Calm Choice",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = AbleyInk)
        )
        Text(
            text = "When we feel overwhelmed or body is buzzing, what helps us feel calm?",
            style = MaterialTheme.typography.bodyMedium.copy(color = AbleyInk.copy(alpha = 0.8f), textAlign = TextAlign.Center),
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("🌬️ Take 3 slow flower-breaths", "🧸 Squish a soft sensory pillow", "🏃 Run fast outside").forEach { option ->
                Button(
                    onClick = { onSuccess(option) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = option, color = AbleyInk, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun CommunicationMiniGame(onSuccess: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Choice & Connect",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = AbleyInk)
        )
        Text(
            text = "Aarav is thirsty after jumping. Tap the card he can show mom:",
            style = MaterialTheme.typography.bodyMedium.copy(color = AbleyInk.copy(alpha = 0.8f), textAlign = TextAlign.Center),
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("💧 Water cup", "🎨 Paint brush", "🛏️ Nap time").forEach { choice ->
                Button(
                    onClick = { if (choice.contains("Water")) onSuccess(choice) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                ) {
                    Text(text = choice, color = AbleyInk, fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
private fun ThinkingMiniGame(onSuccess: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Pattern Master",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = AbleyInk)
        )
        Text(
            text = "Look at the pattern: 🟡 🔺 🟡 🔺. What comes next?",
            style = MaterialTheme.typography.bodyMedium.copy(color = AbleyInk.copy(alpha = 0.8f), textAlign = TextAlign.Center),
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("🟡 Circle", "🔺 Triangle", "🟦 Square").forEach { choice ->
                Button(
                    onClick = { if (choice.contains("Circle")) onSuccess(choice) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Text(text = choice, color = AbleyInk, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun CreativityMiniGame(onSuccess: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Rhythm Tap Canvas",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = AbleyInk)
        )
        Text(
            text = "Tap the bells in rhythm to create a sunny morning harmony:",
            style = MaterialTheme.typography.bodyMedium.copy(color = AbleyInk.copy(alpha = 0.8f), textAlign = TextAlign.Center),
            modifier = Modifier.padding(vertical = 12.dp)
        )

        var taps by remember { mutableIntStateOf(0) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            listOf("🔔 High", "🎵 Medium", "🥁 Low").forEach { instrument ->
                Button(
                    onClick = {
                        taps++
                        if (taps >= 3) onSuccess("Harmony made")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AbleyTeal),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text(text = instrument, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        Text(
            text = "Notes played: $taps / 3",
            style = MaterialTheme.typography.bodySmall.copy(color = AbleyInk.copy(alpha = 0.6f))
        )
    }
}

@Composable
private fun EverydaySkillsMiniGame(onSuccess: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Brushing Teeth Routine",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = AbleyInk)
        )
        Text(
            text = "Before we start brushing our teeth, what do we put on the brush?",
            style = MaterialTheme.typography.bodyMedium.copy(color = AbleyInk.copy(alpha = 0.8f), textAlign = TextAlign.Center),
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("Toothpaste", "Peanut butter", "Shampoo").forEach { item ->
                Button(
                    onClick = { if (item == "Toothpaste") onSuccess(item) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(text = item, color = AbleyInk, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}
