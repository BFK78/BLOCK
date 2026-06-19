package com.basim.block.core.designkit.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.designkit.designsystem.theme.LocalDimens

/**
 * BLOCK logo lockup — the stair-stacked block glyph above the tracked "BLOCK" wordmark.
 * Mirrors the Figma `Logo` component (node 9:62). Colors bind to the M3 scheme, so the same
 * composable serves the On dark / On light themes (ink glyph + wordmark via [primary], gold
 * milestone via [tertiary]) without per-mode branches.
 *
 * "BLOCK" is the brand wordmark (not localizable copy), so it is intentionally a literal here.
 */
@Composable
fun BlockLogo(modifier: Modifier = Modifier) {
    val dimens = LocalDimens.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimens.spacing16),
    ) {
        BlockGlyph()
        Text(
            text = "BLOCK",
            // titleLarge is Inter Semi Bold; tune size + tracking to match the logo lockup.
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 26.sp,
                letterSpacing = 0.14.em,
            ),
            color = MaterialTheme.colorScheme.primary,
        )
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
private fun Block(x: Dp, y: Dp, color: Color) {
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
private fun BlockLogoPreview() {
    BlockTheme {
        BlockBackground(modifier = Modifier.size(160.dp)) {
            Box(contentAlignment = Alignment.Center) {
                BlockLogo()
            }
        }
    }
}
