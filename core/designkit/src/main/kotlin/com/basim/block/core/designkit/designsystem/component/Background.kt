package com.basim.block.core.designkit.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.designkit.designsystem.theme.LocalBackgroundTheme
import com.basim.block.core.designkit.designsystem.theme.GradientColors
import com.basim.block.core.designkit.designsystem.theme.LocalGradientColors
import kotlin.math.tan

@Composable
fun BlockBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    val color = LocalBackgroundTheme.current.color
    val tonalElevation = LocalBackgroundTheme.current.tonalElevation

    Surface(
        color = if (color == Color.Unspecified) Color.Transparent else color,
        tonalElevation = if (tonalElevation == Dp.Unspecified) 0.dp else tonalElevation,
        modifier = modifier.fillMaxSize()
    ) {
        CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
            content()
        }
    }
}

/**
 * rememberUpdatedState => if we are using remember or mutableStateOf, and use the value inside a sideEffect like launchedEffect
 * then we will not be guaranteed to get the latest value, because the sideEffect will take the value from the state
 * when the sideEffect is created, so if there is 5000 millis delay in sideEffect before accessing the state
 * and the state changes during that time, the sideEffect will run with the old state which can cause issues.
 * */
@Composable
fun BlockGradientBackground(
    modifier: Modifier = Modifier,
    gradientColors: GradientColors = LocalGradientColors.current,
    content: @Composable () -> Unit
) {

    val currentTopColor by rememberUpdatedState(newValue = gradientColors.top)
    val currentBottomColor by rememberUpdatedState(newValue = gradientColors.bottom)

    Surface(
        color = if (gradientColors.container == Color.Unspecified)
            Color.Transparent
        else
            gradientColors.container,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {

                    val offset = size.height * tan(
                        Math
                            .toRadians(11.06)
                            .toFloat()
                    )

                    val start = Offset(size.width / 2 + offset / 2, 0f)
                    val end = Offset(size.width / 2 - offset / 2, size.height)

                    // Create the top gradient that fades out after the halfway point vertically
                    val topGradient = Brush.linearGradient(
                        0f to if (currentTopColor == Color.Unspecified)
                            Color.Transparent
                        else
                            currentTopColor,
                        0.724f to Color.Transparent,
                        start = start,
                        end = end
                    )

                    val bottomGradient = Brush.linearGradient(
                        0.2552f to Color.Transparent,
                        1f to if (currentBottomColor == Color.Unspecified)
                            Color.Transparent
                        else
                            currentBottomColor,

                        )

                    onDrawBehind {
                        // There is overlap here, so order is important
                        drawRect(topGradient)
                        drawRect(bottomGradient)
                    }
                }
        ) {
            content()
        }
    }
}

/**
 * Multi preview annotation that represents light and dark themes. Add this annotation to a
 * composable to render the both themes.
 */
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
//annotation class ThemePreviews

// Commenting above code because we have a built in method.


@PreviewLightDark
@Composable
fun BackgroundDynamic() {
    BlockTheme(disableDynamicTheme = true) {
        BlockBackground(Modifier.size(100.dp), content = {})
    }
}


@PreviewLightDark
@Composable
fun GradientBackgroundDynamic() {
    BlockTheme(disableDynamicTheme = true) {
        BlockGradientBackground(Modifier.size(100.dp), content = {})
    }
}