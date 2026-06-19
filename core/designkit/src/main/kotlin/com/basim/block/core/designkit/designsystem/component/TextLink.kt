package com.basim.block.core.designkit.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.theme.BlockTheme

/**
 * Inline emerald (`secondary`) text link. Pairs with body/caption copy for footer actions, terms,
 * and "forgot password". Mirrors the Figma `Link` component (node 27:14).
 */
@Composable
fun BlockTextLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier.clickable(onClick = onClick),
    )
}

@PreviewLightDark
@Composable
private fun BlockTextLinkPreview() {
    BlockTheme {
        BlockBackground(modifier = Modifier.padding(16.dp)) {
            BlockTextLink(text = "Forgot password?", onClick = {})
        }
    }
}
