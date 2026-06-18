package com.basim.block.core.designkit.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spacing, corner-radius and sizing tokens from the Figma foundation (`dimensions` collection).
 * Exposed as an extended theme token via [LocalDimens] (see Theme.kt), mirroring the
 * [GradientColors] / [TintTheme] pattern.
 */
@Immutable
data class Dimens(
    // spacing scale
    val spacing2: Dp = 2.dp,
    val spacing4: Dp = 4.dp,
    val spacing8: Dp = 8.dp,
    val spacing12: Dp = 12.dp,
    val spacing16: Dp = 16.dp,
    val spacing20: Dp = 20.dp,
    val spacing24: Dp = 24.dp,
    val spacing32: Dp = 32.dp,
    val spacing40: Dp = 40.dp,
    val spacing48: Dp = 48.dp,
    val spacing56: Dp = 56.dp,
    val spacing64: Dp = 64.dp,
    // corner radius scale
    val radiusXs: Dp = 8.dp,
    val radiusSm: Dp = 12.dp,
    val radiusMd: Dp = 16.dp,
    val radiusLg: Dp = 20.dp,
    val radiusXl: Dp = 28.dp,
    val radius2xl: Dp = 36.dp,
    val radiusFull: Dp = 999.dp,
    // sizing
    val touchTarget: Dp = 48.dp,
    val controlHeight: Dp = 56.dp,
)

/**
 * A composition local for [Dimens]. See Gradient.kt for the staticCompositionLocalOf rationale.
 */
val LocalDimens = staticCompositionLocalOf { Dimens() }
