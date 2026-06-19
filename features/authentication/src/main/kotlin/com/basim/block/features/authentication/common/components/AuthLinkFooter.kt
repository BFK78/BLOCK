package com.basim.block.features.authentication.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.component.BlockTextLink

/**
 * Centered footer prompt + inline link shared by the auth screens (e.g. "New here? Create an
 * account"). The 6dp gap matches the Figma footer (no spacing token covers 6).
 */
@Composable
fun AuthLinkFooter(
    prompt: String,
    linkLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = prompt,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        BlockTextLink(text = linkLabel, onClick = onClick)
    }
}
