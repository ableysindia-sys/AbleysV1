package com.example.ui.screens.onboarding

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleyInk
import com.example.ui.theme.AbleyIvory
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal

/** What a parent tells us before they start. Nothing here is required except a name. */
data class OnboardingResult(
    val childName: String,
    val birthMonth: String,
    val avatarEmoji: String,
    val photoUri: Uri?,
    val supportLayerEnabled: Boolean
)

private val MONTHS = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

private val AVATARS = listOf("🦁", "🐢", "🦊", "🐛", "🐙", "🦋", "🐼", "🦉")

/**
 * First run.
 *
 * Without this the app opened on a child called Aarav with 1,240 XP and a twelve-day streak
 * nobody had earned. Those numbers come from the spec mockups and they are fine in a deck; in a
 * shipped app they tell a parent on day one that the progress it shows them is decoration.
 *
 * So a real child starts at zero, and the sample data is behind an explicitly labelled door for
 * demos rather than being the default state.
 */
@Composable
fun OnboardingScreen(
    onComplete: (OnboardingResult) -> Unit,
    onExploreWithSampleData: () -> Unit,
    // Reused when a parent adds a second child, where "sample data" is not what the escape hatch
    // does and saying so would be a lie in a two-word button.
    secondaryLabel: String = "Look around with sample data first",
    headline: String = "Who are we growing with?",
    primaryLabel: String = "Start growing together",
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var birthMonth by remember { mutableStateOf("") }
    var avatar by remember { mutableStateOf(AVATARS.first()) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var supportLayer by remember { mutableStateOf(false) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> if (uri != null) photoUri = uri }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AbleyIvory)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .testTag("onboarding_screen")
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "ABLEY'S",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            letterSpacing = 2.sp,
            color = AbleyCoral
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = headline,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = AbleyInk
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Just a first name is enough. Everything else can wait, and nothing here " +
                "leaves this phone.",
            style = MaterialTheme.typography.bodyMedium,
            color = AbleyInk.copy(alpha = 0.65f)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(AbleySand.copy(alpha = 0.5f))
                .border(1.dp, AbleySand, CircleShape)
                .clickable {
                    photoPicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
                .testTag("onboarding_photo"),
            contentAlignment = Alignment.Center
        ) {
            val picked = photoUri
            if (picked != null) {
                AsyncImage(
                    model = picked,
                    contentDescription = "Child's photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(96.dp).clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.AddAPhoto,
                    contentDescription = "Add a photo",
                    tint = AbleyCoral,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("First name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("onboarding_name")
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "PICK A COMPANION",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = AbleyInk.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(AVATARS) { emoji ->
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(if (emoji == avatar) AbleyCoral.copy(alpha = 0.16f) else Color.White)
                        .border(
                            width = if (emoji == avatar) 2.dp else 1.dp,
                            color = if (emoji == avatar) AbleyCoral else AbleySand,
                            shape = CircleShape
                        )
                        .clickable { avatar = emoji },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 20.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "BIRTH MONTH, IF YOU LIKE",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = AbleyInk.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(MONTHS) { month ->
                val selected = month == birthMonth
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (selected) AbleyCoral else Color.White)
                        .border(1.dp, if (selected) AbleyCoral else AbleySand, RoundedCornerShape(20.dp))
                        .clickable { birthMonth = if (selected) "" else month }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = month.take(3),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selected) Color.White else AbleyInk
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, AbleySand, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Additional Support",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Home programmes and parent stories, for families who want them. " +
                        "You can turn this on later.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AbleyInk.copy(alpha = 0.6f)
                )
            }
            Switch(
                checked = supportLayer,
                onCheckedChange = { supportLayer = it },
                colors = SwitchDefaults.colors(checkedTrackColor = AbleyTeal),
                modifier = Modifier.testTag("onboarding_support_toggle")
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                onComplete(
                    OnboardingResult(
                        childName = name.trim(),
                        birthMonth = birthMonth,
                        avatarEmoji = avatar,
                        photoUri = photoUri,
                        supportLayerEnabled = supportLayer
                    )
                )
            },
            enabled = name.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = AbleyCoral),
            shape = RoundedCornerShape(26.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("onboarding_start")
        ) {
            Text(
                text = primaryLabel,
                color = Color.White,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Sample data is a demo door, labelled as one. It is not the default state, because a
        // parent who sees invented progress on day one stops believing the real progress later.
        TextButton(
            onClick = onExploreWithSampleData,
            modifier = Modifier.fillMaxWidth().testTag("onboarding_sample_data")
        ) {
            Text(
                text = secondaryLabel,
                color = AbleyInk.copy(alpha = 0.55f),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
