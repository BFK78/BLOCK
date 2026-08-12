package com.basim.block.core.ui.animation

import android.provider.Settings
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

const val ENTRANCE_DURATION_MILLIS = 320
const val ENTRANCE_STEP_MILLIS = 50

/**
 * Flips false -> true once, after the first composition.
 * That flip is what gives [entrance] something to animate towards.
 *
 * rememberSaveable so a config change doesn't replay the intro.
 */
@Composable
fun rememberEntranceTrigger(): Boolean {
    var started by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) { started = true }
    return started
}

/** True when the user has turned animations off in Developer/Accessibility settings. */
@Composable
fun rememberReducedMotion(): Boolean {
    val context = LocalContext.current
    return remember(context) {
        Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f,
        ) == 0f
    }
}

/**
 * Fade + rise into place.
 *
 * Only touches alpha/translationY inside a graphicsLayer lambda, so the
 * animation runs in the draw phase — no recomposition, no relayout per frame.
 *
 * Apply this BEFORE .background()/.clip() if you want the background to fade too.
 *
 * @param visible drive this from [rememberEntranceTrigger].
 * @param order stagger position; each step delays the start by [ENTRANCE_STEP_MILLIS].
 * @param rise how far the element travels up as it fades in.
 * @param onSettled fired once, when this element finishes settling into place. Put it on the
 *   highest-[order] element to learn when the whole staggered intro is done. Under reduced motion the snap
 *   still fires it (immediately); it does not fire when there's nothing to animate (e.g. the
 *   element is restored already-visible after a config change).
 */
@Composable
fun Modifier.entrance(
    visible: Boolean,
    order: Int = 0,
    rise: Dp = 16.dp,
    onSettled: (() -> Unit)? = null,
): Modifier {
    val reduced = rememberReducedMotion()
    // Note: no `by` delegate — we keep the State object and read it inside the
    // graphicsLayer lambda so reads are deferred to the draw phase.
    val progress = animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = if (reduced) {
            snap()
        } else {
            tween(
                durationMillis = ENTRANCE_DURATION_MILLIS,
                delayMillis = order * ENTRANCE_STEP_MILLIS,
                easing = EaseOutCubic, // decelerate: fast in, gentle settle
            )
        },
        label = "entrance-$order",
        finishedListener = { end -> if (end == 1f) onSettled?.invoke() },
    )
    val risePx = with(LocalDensity.current) { rise.toPx() }

    return this.graphicsLayer {
        alpha = progress.value
        translationY = (1f - progress.value) * risePx
    }
}
