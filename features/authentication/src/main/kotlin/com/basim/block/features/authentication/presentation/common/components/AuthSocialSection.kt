package com.basim.block.features.authentication.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.component.BlockLabeledDivider
import com.basim.block.core.designkit.designsystem.component.BlockSocialButton
import com.basim.block.core.designkit.designsystem.icon.BlockIcons
import com.basim.block.features.authentication.R

/**
 * Shared social-auth block used by both the Login and Sign Up screens: an "or" divider followed by
 * the Google and Apple [BlockSocialButton]s. The Google glyph keeps its brand colors (untinted); the
 * Apple glyph follows `onSurface`.
 */
@Composable
fun AuthSocialSection(
    onGoogle: () -> Unit,
    onApple: () -> Unit,
    modifier: Modifier = Modifier,
    verticalSpacing: Dp = 12.dp,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
    ) {
        BlockLabeledDivider(label = stringResource(R.string.features_authentication_or))
        BlockSocialButton(
            label = stringResource(R.string.features_authentication_continue_google),
            onClick = onGoogle,
            leadingIcon = {
                Icon(
                    imageVector = BlockIcons.GoogleIcon,
                    contentDescription = null,
                    tint = Color.Unspecified, // keep Google's brand colors
                    modifier = Modifier.size(20.dp),
                )
            },
        )
        BlockSocialButton(
            label = stringResource(R.string.features_authentication_continue_apple),
            onClick = onApple,
            leadingIcon = {
                Icon(
                    imageVector = BlockIcons.AppleIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp),
                )
            },
        )
    }
}
