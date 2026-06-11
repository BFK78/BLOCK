package com.basim.block.core.designkit.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * A class to model gradient color values for Block.
 *
 * @param top The top gradient color to be rendered.
 * @param bottom The bottom gradient color to be rendered.
 * @param container The container gradient color over which the gradient will be rendered.
 */
@Immutable
data class GradientColors(
    val top: Color = Color.Unspecified,
    val bottom: Color = Color.Unspecified,
    val container: Color = Color.Unspecified,
)

/**
 * A composition local for [GradientColors].
 */

// staticCompositionLocalOf => Used when you don't want to pass the values without parameters for composable function
// We can access this value within the compose which are inside the block of `CompositionLocalProvider`,
// So this pattern is used when multiple composable need this value, but don't want to explicitly passed as parameter.
// Also another thing to note is that, the read value is not tracked by the composer, so any change in value will re compose the entire composable.
// So use non frequent changing values for this, but when it changes it should change the whole.
// OR explore `compositionLocalOf`
val LocalGradientColors = staticCompositionLocalOf { GradientColors() }