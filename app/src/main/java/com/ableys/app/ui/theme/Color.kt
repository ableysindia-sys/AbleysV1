package com.ableys.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Abley's Strict Brand Palette (Coral #EE4A41, Teal #1F7A74, Ivory #F7F6F2, Sand #EFEAE0, Ink #1F1B19)
val AbleyCoral = Color(0xFFEE4A41)
val AbleyTeal = Color(0xFF1F7A74)
val AbleyIvory = Color(0xFFF7F6F2)
val AbleySand = Color(0xFFEFEAE0)
val AbleyInk = Color(0xFF1F1B19)

// Complementary tints & functional UI tokens derived from core palette
val AbleyCoralLight = Color(0xFFFDF0EF)
val AbleyCoralDark = Color(0xFFD63B32)
val AbleyTealLight = Color(0xFFE9F4F3)
val AbleyTealDark = Color(0xFF13534F)
val AbleySandLight = Color(0xFFFAF8F4)
val AbleySandDark = Color(0xFFE2DCD0)
val AbleyInkLight = Color(0xFF38322F)
val AbleyInkDark = Color(0xFF141211)
val AbleySurfaceDark = Color(0xFF24201E)
val AbleySurfaceDarkCard = Color(0xFF2E2927)
val AbleyGold = Color(0xFFECA82B)
val AbleyGoldLight = Color(0xFFFFF8E7)
val AbleyBorder = Color(0xFFE5E0D7)
val AbleyMutedText = Color(0xFF756F6B)
val AbleySuccess = Color(0xFF2E7D32)

/**
 * Immutable Brand Palette representation providing direct, type-safe access
 * to the five core brand colors and their complementary tonal variants.
 */
@Immutable
data class AbleyBrandPalette(
    val coral: Color = AbleyCoral,
    val teal: Color = AbleyTeal,
    val ivory: Color = AbleyIvory,
    val sand: Color = AbleySand,
    val ink: Color = AbleyInk,
    val coralLight: Color = AbleyCoralLight,
    val coralDark: Color = AbleyCoralDark,
    val tealLight: Color = AbleyTealLight,
    val tealDark: Color = AbleyTealDark,
    val sandLight: Color = AbleySandLight,
    val sandDark: Color = AbleySandDark,
    val mutedText: Color = AbleyMutedText,
    val border: Color = AbleyBorder
)

val LocalAbleyPalette = staticCompositionLocalOf { AbleyBrandPalette() }

