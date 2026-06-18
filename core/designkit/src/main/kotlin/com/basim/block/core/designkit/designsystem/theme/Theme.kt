package com.basim.block.core.designkit.designsystem.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

// Color schemes map the BLOCK Figma foundation (`color` collection) onto the Material 3 roles.
// Dark = Figma "Dark" mode, Light = Figma "Light" mode; both resolve from the same palette
// constants (Color.kt) so the two modes stay in parity.

@VisibleForTesting
val DarkColorScheme = darkColorScheme(
    primary = Ink50,
    onPrimary = Ink950,
    primaryContainer = Ink700,
    onPrimaryContainer = Ink50,
    secondary = Em300,
    onSecondary = Em900,
    secondaryContainer = Em700,
    onSecondaryContainer = Em100,
    tertiary = Ch300,
    onTertiary = Color(0xFF2A2102),
    tertiaryContainer = Color(0xFF5C4A1E),
    onTertiaryContainer = Ch200,
    background = Ink950,
    onBackground = Ink50,
    surface = Ink900,
    onSurface = Ink50,
    surfaceContainerLowest = Ink950,
    surfaceContainerLow = Ink800,
    surfaceContainer = Color(0xFF201E1A),
    surfaceContainerHigh = Ink700,
    surfaceContainerHighest = Color(0xFF353128),
    surfaceVariant = Ink700,
    onSurfaceVariant = Ink300,
    outline = Color(0xFF4A463E),
    outlineVariant = Ink700,
    error = Red500Dark,
    onError = Color(0xFF2A0A07),
    errorContainer = Color(0xFF5A1B16),
    onErrorContainer = Color(0xFFF7D8D5),
    inverseSurface = Ink50,
    inverseOnSurface = Ink800,
    scrim = Scrim,
)

@VisibleForTesting
val LightColorScheme = lightColorScheme(
    primary = Ink950,
    onPrimary = Ink0,
    primaryContainer = Ink800,
    onPrimaryContainer = Ink50,
    secondary = Em500,
    onSecondary = PureWhite,
    secondaryContainer = Em100,
    onSecondaryContainer = Em900,
    tertiary = Ch500,
    onTertiary = Color(0xFF1A1916),
    tertiaryContainer = Ch200,
    onTertiaryContainer = Color(0xFF463612),
    background = Ink0,
    onBackground = Ink900,
    surface = PureWhite,
    onSurface = Ink900,
    surfaceContainerLowest = PureWhite,
    surfaceContainerLow = Color(0xFFF7F5EF),
    surfaceContainer = Ink50,
    surfaceContainerHigh = Ink100,
    surfaceContainerHighest = Color(0xFFE1DDD2),
    surfaceVariant = Ink100,
    onSurfaceVariant = Ink500,
    outline = Ink300,
    outlineVariant = Ink200,
    error = Red500,
    onError = PureWhite,
    errorContainer = Red100,
    onErrorContainer = Red900,
    inverseSurface = Ink700,
    inverseOnSurface = Ink50,
    scrim = Scrim,
)

@Composable
fun BlockTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    disableDynamicTheme: Boolean = true,
    content: @Composable () -> Unit
) {

    val colorScheme = when {
        !disableDynamicTheme && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> if (darkTheme) DarkColorScheme else LightColorScheme
    }

    //Gradient Colors
    val emptyGradientColors = GradientColors(container = colorScheme.surfaceColorAtElevation(2.dp))
    val defaultGradientColors = GradientColors(
        top = colorScheme.inverseOnSurface,
        bottom = colorScheme.primaryContainer,
        container = colorScheme.surface
    )
    val gradientColors = when {
        !disableDynamicTheme && supportsDynamicTheming() -> emptyGradientColors
        else -> defaultGradientColors
    }

    // Background Theme
    val backgroundTheme = BackgroundTheme(
        color = colorScheme.surface,
        tonalElevation = 2.dp,
    )

    // Tint Theme
    val tintTheme = when {
        !disableDynamicTheme && supportsDynamicTheming() -> TintTheme(colorScheme.primary)
        else -> TintTheme()
    }

    // Chart / limit colors — categorical & heatmap are fixed; track and limit-calm follow the
    // active scheme (Figma aliases them to surface-container-highest / secondary).
    val chartColors = BlockChartColors(
        track = colorScheme.surfaceContainerHighest,
        limitCalm = colorScheme.secondary,
    )

    CompositionLocalProvider(
        LocalGradientColors provides gradientColors,
        LocalBackgroundTheme provides backgroundTheme,
        LocalTintTheme provides tintTheme,
        LocalDimens provides Dimens(),
        LocalChartColors provides chartColors,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = BlockTypography,
            content = content
        )
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S