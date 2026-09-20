package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

/**
 * Abley's Official Light Color Scheme
 * Strictly enforcing:
 * - Coral: #EE4A41
 * - Teal:  #1F7A74
 * - Ivory: #F7F6F2
 * - Sand:  #EFEAE0
 * - Ink:   #1F1B19
 */
val AbleyLightColorScheme = lightColorScheme(
    primary = AbleyCoral,              // #EE4A41
    onPrimary = Color.White,
    primaryContainer = AbleyCoralLight,
    onPrimaryContainer = AbleyCoralDark,

    secondary = AbleyTeal,            // #1F7A74
    onSecondary = Color.White,
    secondaryContainer = AbleyTealLight,
    onSecondaryContainer = AbleyTealDark,

    tertiary = AbleyGold,
    onTertiary = Color.White,
    tertiaryContainer = AbleyGoldLight,
    onTertiaryContainer = AbleyInk,

    background = AbleyIvory,          // #F7F6F2
    onBackground = AbleyInk,          // #1F1B19

    surface = Color.White,
    onSurface = AbleyInk,             // #1F1B19

    surfaceVariant = AbleySand,       // #EFEAE0
    onSurfaceVariant = AbleyInk,      // #1F1B19

    surfaceContainerLowest = Color.White,
    surfaceContainerLow = AbleyIvory, // #F7F6F2
    surfaceContainer = AbleySandLight,
    surfaceContainerHigh = AbleySand, // #EFEAE0
    surfaceContainerHighest = AbleySandDark,

    outline = AbleyBorder,
    outlineVariant = AbleySand,       // #EFEAE0

    inverseSurface = AbleyInk,        // #1F1B19
    inverseOnSurface = AbleyIvory,    // #F7F6F2
    inversePrimary = AbleyCoralLight
)

/**
 * Abley's Official Dark Color Scheme
 * Maintaining contrast and color-tone fidelity for low-light environments.
 */
val AbleyDarkColorScheme = darkColorScheme(
    primary = AbleyCoral,             // #EE4A41
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4A1A17),
    onPrimaryContainer = AbleyCoralLight,

    secondary = Color(0xFF2EA39B),    // High-contrast accessible Teal
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF133B38),
    onSecondaryContainer = Color(0xFFB4E3E0),

    tertiary = AbleyGold,
    onTertiary = Color.Black,
    tertiaryContainer = AbleySurfaceDarkCard,
    onTertiaryContainer = AbleyGold,

    background = AbleyInkDark,
    onBackground = AbleyIvory,        // #F7F6F2

    surface = AbleySurfaceDark,
    onSurface = AbleyIvory,           // #F7F6F2

    surfaceVariant = AbleySurfaceDarkCard,
    onSurfaceVariant = AbleySand,     // #EFEAE0

    surfaceContainerLowest = AbleyInkDark,
    surfaceContainerLow = AbleySurfaceDark,
    surfaceContainer = AbleySurfaceDarkCard,
    surfaceContainerHigh = Color(0xFF383230),
    surfaceContainerHighest = Color(0xFF423B38),

    outline = Color(0xFF4A4440),
    outlineVariant = Color(0xFF383330),

    inverseSurface = AbleyIvory,      // #F7F6F2
    inverseOnSurface = AbleyInk,      // #1F1B19
    inversePrimary = AbleyCoral
)

/**
 * Friendly, soft rounded shapes customized for pediatric and developmental care.
 */
val AbleyShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

/**
 * Static accessor for Abley's theme tokens.
 */
object AbleyTheme {
    val palette: AbleyBrandPalette
        @Composable
        get() = LocalAbleyPalette.current

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes

    val poppins: FontFamily
        get() = PoppinsFontFamily

    val dmSans: FontFamily
        get() = DmSansFontFamily
}

@Composable
fun AbleysTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) AbleyDarkColorScheme else AbleyLightColorScheme
    val brandPalette = remember(darkTheme) {
        if (darkTheme) {
            AbleyBrandPalette(
                coral = AbleyCoral,
                teal = Color(0xFF2EA39B),
                ivory = AbleyIvory,
                sand = AbleySand,
                ink = AbleyIvory,
                coralLight = Color(0xFF4A1A17),
                coralDark = AbleyCoral,
                tealLight = Color(0xFF133B38),
                tealDark = Color(0xFF0F4440),
                sandLight = AbleySurfaceDarkCard,
                sandDark = Color(0xFF423B38),
                mutedText = Color(0xFFA8A29D),
                border = Color(0xFF3D3734)
            )
        } else {
            AbleyBrandPalette()
        }
    }

    CompositionLocalProvider(
        LocalAbleyPalette provides brandPalette
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AbleyTypography,
            shapes = AbleyShapes,
            content = content
        )
    }
}

// Retain alias for any existing template callers
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AbleysTheme(darkTheme = darkTheme, content = content)
}

