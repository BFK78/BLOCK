package com.basim.block.features.authentication.presentation.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.basim.block.core.designkit.designsystem.component.BlockBackground
import com.basim.block.core.designkit.designsystem.icon.BlockIcons
import com.basim.block.core.designkit.designsystem.theme.BlockTheme

/**
 * Slim top bar shared by the auth screens: a 48dp back icon-button on the leading edge, a centered
 * uppercase eyebrow label, and a 48dp spacer on the trailing edge so the eyebrow stays optically
 * centered. Mirrors the Figma `TopAppBar` component (node 36:54).
 */
@Composable
fun AuthTopAppBar(
    eyebrow: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    showBack: Boolean = true,
    contentDescription: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showBack) {
            IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                Icon(
                    imageVector = BlockIcons.ArrowBack,
                    contentDescription = contentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        } else {
            Box(modifier = Modifier.size(48.dp))
        }
        Text(
            text = eyebrow,
            style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 0.08.em),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
        )
        // Balancing spacer mirrors the back button so the eyebrow is centered.
        Box(modifier = Modifier.size(48.dp))
    }
}

@PreviewLightDark
@Composable
private fun AuthTopAppBarPreview() {
    BlockTheme {
        BlockBackground(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            AuthTopAppBar(eyebrow = "GET STARTED", onBack = {})
        }
    }
}