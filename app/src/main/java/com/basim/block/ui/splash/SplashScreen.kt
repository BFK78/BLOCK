package com.basim.block.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.basim.block.core.designkit.designsystem.theme.BlockTheme

// This is not being used currently - because we will be showing system splash screen for now and this will be reserved for future ref.

/**
 * Splash screen — the exact BLOCK design from the Figma Auth Flows page ("01 Splash").
 *
 * Every color binds to [MaterialTheme.colorScheme], so this single composable reproduces both the
 * Dark and Light Figma variants (BlockTheme follows the system theme): ink/cream background, the
 * three-block glyph (two ink squares + one gold milestone), the "BLOCK" wordmark and the tagline.
 *
 * Cosmetic only — there is no startup logic or onward navigation yet.
 */
@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BlockGlyph()
            Spacer(Modifier.height(16.dp))
            Text(
                text = "BLOCK",
                // titleLarge is Inter Semi Bold; tune size + tracking to match the logo lockup.
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 26.sp,
                    letterSpacing = 0.14.em,
                ),
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Wealth, block by block.",
                style = MaterialTheme.typography.bodySmall, // Inter Regular 12sp / 16
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/**
 * The BLOCK logo glyph: a 32dp grid of three 14dp rounded squares (r=4) with 2dp gaps; the
 * top-left quadrant is empty. Bottom-left + bottom-right use [primary]; the top-right "milestone"
 * uses [tertiary] (gold). Offsets match the Figma component exactly.
 */
@Composable
private fun BlockGlyph(modifier: Modifier = Modifier) {
    val ink = MaterialTheme.colorScheme.primary
    val gold = MaterialTheme.colorScheme.tertiary
    Box(modifier = modifier.size(32.dp)) {
        Block(x = 1.dp, y = 17.dp, color = ink)   // bottom-left
        Block(x = 17.dp, y = 17.dp, color = ink)  // bottom-right
        Block(x = 17.dp, y = 1.dp, color = gold)  // top-right milestone
    }
}

@Composable
private fun Block(x: androidx.compose.ui.unit.Dp, y: androidx.compose.ui.unit.Dp, color: Color) {
    Box(
        Modifier
            .offset(x = x, y = y)
            .size(14.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color),
    )
}

@PreviewLightDark
@Composable
private fun SplashScreenLightPreview() {
    BlockTheme {
        SplashScreen()
    }
}
