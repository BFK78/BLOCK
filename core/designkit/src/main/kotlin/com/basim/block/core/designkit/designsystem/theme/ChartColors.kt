package com.basim.block.core.designkit.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Chart / heatmap / spending-limit colors from the Figma foundation (`chart` collection).
 * These have no home in the M3 [androidx.compose.material3.ColorScheme], so they are exposed as an
 * extended theme token via [LocalChartColors] (provided in Theme.kt).
 *
 * `track` and `limitCalm` are mode-dependent in Figma (aliased to surface-container-highest /
 * secondary), so Theme.kt rebuilds this holder from the active color scheme; the categorical and
 * heatmap colors are fixed across modes.
 */
@Immutable
data class BlockChartColors(
    val chart1: Color = Chart1,
    val chart2: Color = Chart2,
    val chart3: Color = Chart3,
    val chart4: Color = Chart4,
    val chart5: Color = Chart5,
    val chart6: Color = Chart6,
    val heat0: Color = Heat0,
    val heat1: Color = Heat1,
    val heat2: Color = Heat2,
    val heat3: Color = Heat3,
    val heat4: Color = Heat4,
    // mode-dependent — defaults match Dark mode; rebuilt from colorScheme in BlockTheme
    val track: Color = Ink700,
    val limitCalm: Color = Em300,
    // fixed
    val limitWarn: Color = Amber500,
    val limitOver: Color = LimitOver,
)

/**
 * A composition local for [BlockChartColors]. See Gradient.kt for the rationale.
 */
val LocalChartColors = staticCompositionLocalOf { BlockChartColors() }
