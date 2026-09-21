package com.ableys.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ableys.app.ui.theme.AbleyInk
import com.ableys.app.ui.theme.AbleySand
import com.ableys.app.ui.theme.AbleyTeal

/**
 * What to use instead, shown level with the product rather than beneath it.
 *
 * Placement is the argument. A family that cannot buy the equipment today is the family most
 * likely to close the app, and an activity that opens with a purchase reads as an advert. Put
 * the substitution alongside and the equipment becomes the better version of something already
 * possible, which is both truer and a better reason to buy it later.
 *
 * [needsInstallation] adds the line that matters in a rented flat: most of these are concrete
 * slab, and a ceiling fixture is a landlord conversation rather than an afternoon.
 */
@Composable
fun HouseholdAlternativeCard(
    alternative: String,
    needsInstallation: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(AbleyTeal.copy(alpha = 0.07f))
            .border(1.dp, AbleyTeal.copy(alpha = 0.22f), RoundedCornerShape(14.dp))
            .padding(14.dp)
            .testTag("household_alternative")
    ) {
        Row {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = null,
                tint = AbleyTeal,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "DON'T HAVE IT? USE WHAT'S AT HOME",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = AbleyTeal
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = alternative,
            style = MaterialTheme.typography.bodySmall,
            color = AbleyInk.copy(alpha = 0.82f)
        )
        if (needsInstallation) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "This equipment needs a ceiling fixture, which usually isn't an option in " +
                    "a rented flat. The version above needs nothing fixed to anything.",
                style = MaterialTheme.typography.bodySmall,
                color = AbleyInk.copy(alpha = 0.55f)
            )
        }
    }
}
