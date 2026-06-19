package com.basim.block.core.designkit.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.icon.BlockIcons
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.designkit.designsystem.theme.LocalDimens

/**
 * Outlined pill for social auth: a full-width 56dp button on an `outline` hairline, with a leading
 * brand glyph (kept full-color) and an `onSurface` label. Mirrors the Figma `Button/Social`
 * component (node 28:18).
 */
@Composable
fun BlockSocialButton(
    label: String,
    onClick: () -> Unit,
    leadingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val dimens = LocalDimens.current
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(dimens.controlHeight),
        enabled = enabled,
        shape = CircleShape,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimens.spacing12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon()
            Text(text = label, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@PreviewLightDark
@Composable
private fun BlockSocialButtonPreview() {
    BlockTheme {
        BlockBackground(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
        ) {
            BlockSocialButton(
                label = "Continue with Apple",
                onClick = {},
                leadingIcon = {
                    androidx.compose.material3.Icon(
                        imageVector = BlockIcons.AppleIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp),
                    )
                },
            )
        }
    }
}
