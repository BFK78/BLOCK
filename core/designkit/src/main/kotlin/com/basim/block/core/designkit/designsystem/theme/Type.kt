package com.basim.block.core.designkit.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.basim.block.core.designsystem.R

/**
 * BLOCK font families — from the Figma foundation. Fraunces (serif) for display/headline,
 * Inter for UI text, Geist Mono for numeric/currency figures. Static weights are bundled in
 * res/font; letter-spacing values are expressed in `.em` to match the Figma percentages.
 */
internal val Fraunces = FontFamily(
    Font(R.font.fraunces_semibold, FontWeight.SemiBold),
)

internal val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
)

internal val GeistMono = FontFamily(
    Font(R.font.geist_mono_medium, FontWeight.Medium),
)

/**
 * Block typography — Figma type scale mapped onto the Material 3 [Typography] roles so existing
 * `MaterialTheme.typography.*` consumers keep working.
 *
 * Mapping: display and headlineLarge use Fraunces (display/xl, lg, md, headline); title and label
 * roles use Inter Semi Bold / Medium; body roles use Inter Regular. headlineMedium/Small and
 * labelSmall have no exact Figma equivalent (interpolated). Geist Mono numeric styles live in
 * [BlockTextStyles].
 */
internal val BlockTypography = Typography(
    // display/xl — Fraunces
    displayLarge = TextStyle(
        fontFamily = Fraunces,
        fontWeight = FontWeight.SemiBold,
        fontSize = 52.sp,
        lineHeight = 56.sp,
        letterSpacing = (-0.015).em,
    ),
    // display/lg — Fraunces
    displayMedium = TextStyle(
        fontFamily = Fraunces,
        fontWeight = FontWeight.SemiBold,
        fontSize = 40.sp,
        lineHeight = 44.sp,
        letterSpacing = (-0.015).em,
    ),
    // display/md — Fraunces
    displaySmall = TextStyle(
        fontFamily = Fraunces,
        fontWeight = FontWeight.SemiBold,
        fontSize = 34.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.01).em,
    ),
    // headline — Fraunces
    headlineLarge = TextStyle(
        fontFamily = Fraunces,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.005).em,
    ),
    // interpolated (no exact Figma token)
    headlineMedium = TextStyle(
        fontFamily = Fraunces,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.005).em,
    ),
    // interpolated (no exact Figma token)
    headlineSmall = TextStyle(
        fontFamily = Fraunces,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp,
    ),
    // title/lg — Inter
    titleLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    // title/md — Inter
    titleMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    // label/lg — Inter (closest small title)
    titleSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.005.em,
    ),
    // body/lg — Inter
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    // body/md — Inter
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    // caption — Inter
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
    ),
    // label/lg — Inter (button)
    labelLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.005.em,
    ),
    // label/md — Inter (navigation items)
    labelMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
    ),
    // interpolated small label (no exact Figma token)
    labelSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.005.em,
    ),
)

/**
 * Geist Mono numeric/currency styles from the Figma foundation (`numeric/lg`, `numeric/md`).
 * These are not Material 3 roles, so they are exposed directly for currency figures, amounts and
 * tabular numbers. Mode-independent — reference as `BlockTextStyles.numericLarge`.
 */
object BlockTextStyles {
    val numericLarge = TextStyle(
        fontFamily = GeistMono,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    )
    val numericMedium = TextStyle(
        fontFamily = GeistMono,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    )
}
