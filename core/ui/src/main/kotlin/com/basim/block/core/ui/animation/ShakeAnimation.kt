package com.basim.block.core.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

const val SHAKE_DURATION_MILLIS = 400

private const val SHAKE_CYCLES = 3f

private val SHAKE_DISTANCE = 8.dp

@Composable
fun Modifier.shake(
    trigger: Int,
    distance: Dp = SHAKE_DISTANCE,
): Modifier {

    val reduced = rememberReducedMotion()
    val distancePx = with(LocalDensity.current) { distance.toPx() }
    val progress = remember { Animatable(1f) }

    LaunchedEffect(trigger) {
        if (trigger == 0 || reduced) return@LaunchedEffect
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = SHAKE_DURATION_MILLIS,
                easing = LinearEasing, // constant tempo; the decay below does the easing
            ),
        )
    }

    return this.graphicsLayer {
        val elapsed = progress.value
        // Sine gives the swing, (1 - elapsed) bleeds the amplitude away so it settles at 0.
        translationX = sin(elapsed * SHAKE_CYCLES * 2f * PI.toFloat()) * distancePx * (1f - elapsed)
    }
}
