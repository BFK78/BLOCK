package com.basim.block.core.designkit.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.designkit.designsystem.theme.LocalDimens

// Figma checkbox box radius is 6dp — between radiusXs (8) and nothing smaller, so it's a literal.
private val BOX_RADIUS = 6.dp
private val BOX_SIZE = 22.dp

/**
 * Square checkbox with a label, sized for a 48dp row touch target. Unchecked = `surfaceContainer`
 * box on an `outline` hairline; checked = `secondary` fill with a check glyph. Mirrors the Figma
 * `Checkbox` component (node 33:52).
 */
@Composable
fun BlockCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val dimens = LocalDimens.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                enabled = enabled,
                role = Role.Checkbox,
            ),
        horizontalArrangement = Arrangement.spacedBy(dimens.spacing12),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(BOX_SIZE)
                .background(
                    color = if (checked) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.surfaceContainer
                    },
                    shape = RoundedCornerShape(BOX_RADIUS),
                )
                .border(
                    width = 1.5.dp,
                    color = if (checked) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    shape = RoundedCornerShape(BOX_RADIUS),
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
    }
}

@PreviewLightDark
@Composable
private fun BlockCheckboxPreview() {
    BlockTheme {
        BlockBackground(
            modifier = Modifier
                .fillMaxWidth()
                .size(width = 300.dp, height = 60.dp)
        ) {
            BlockCheckbox(
                checked = true,
                onCheckedChange = {},
                label = "By continuing you agree to the Terms and Privacy Policy.",
            )
        }
    }
}
