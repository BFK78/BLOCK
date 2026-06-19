package com.basim.block.core.designkit.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.designkit.designsystem.theme.LocalDimens

/**
 * Onboarding page indicator — a row of dots where the selected page elongates into an emerald pill.
 * Mirrors the Figma `PageIndicator` component (node 34:76): inactive dots are 8×8
 * `surfaceContainerHighest`, the active dot is a 24×8 `secondary` pill, gap 8.
 */
@Composable
fun BlockPageIndicator(
    pageCount: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
) {
    val dimens = LocalDimens.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dimens.spacing8),
    ) {
        repeat(pageCount) { index ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .size(
                        width = if (selected) ACTIVE_DOT_WIDTH else dimens.spacing8,
                        height = dimens.spacing8,
                    )
                    .clip(RoundedCornerShape(dimens.radiusFull))
                    .background(
                        if (selected) {
                            MaterialTheme.colorScheme.secondary
                        } else {
                            MaterialTheme.colorScheme.surfaceContainerHighest
                        },
                    ),
            )
        }
    }
}

// Active dot pill width — no exact Dimens token (3× the 8dp dot).
private val ACTIVE_DOT_WIDTH = 24.dp

@PreviewLightDark
@Composable
fun BlockPageIndicatorPreview() {
    BlockTheme {
        BlockBackground(modifier = Modifier.size(120.dp, 40.dp)) {
            BlockPageIndicator(pageCount = 3, selectedIndex = 0)
        }
    }
}
