package com.basim.block.features.authentication.presentation.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.component.BlockBackground
import com.basim.block.core.designkit.designsystem.component.BlockButton
import com.basim.block.core.designkit.designsystem.component.BlockLogo
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.designkit.designsystem.theme.LocalDimens
import com.basim.block.features.authentication.R

/**
 * Auth Landing — pixel-built from Figma. The entry point after onboarding: the BLOCK logo lockup
 * and a serif welcome line float in the upper portion, with a full-width primary "Create a profile"
 * CTA and a "Already have an account? Sign in" footer pinned to the bottom. Flexible spacers above
 * and below the logo/welcome block center it in the space above the CTA.
 *
 * Figma: https://www.figma.com/design/R1bw3ysZmoZ83l0VCfUTCt/?node-id=42-33
 * Page: Auth Flows (2:3)  ·  Mode: Dark (canonical) — binds to M3 roles, so it also serves Light.
 */
@Composable
fun AuthLandingScreen(
    modifier: Modifier = Modifier,
    onCreateProfile: () -> Unit = {},
    onSignIn: () -> Unit = {},
) {
    val dimens = LocalDimens.current
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = dimens.spacing24, vertical = dimens.spacing32),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimens.spacing16),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        BlockLogo()

        Text(
            text = stringResource(R.string.features_authentication_landing_welcome),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(1f))

        BlockButton(
            onClick = onCreateProfile,
            shape = CircleShape, // filled pill CTA (Figma Button/Primary, radius-full)
            modifier = Modifier
                .fillMaxWidth()
                .height(dimens.controlHeight),
        ) {
            Text(
                text = stringResource(R.string.features_authentication_landing_cta_create),
                style = MaterialTheme.typography.labelLarge,
            )
        }

        Row(
            // Figma footer gap is 6px — no spacing token covers 6, so this is an intentional literal.
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.features_authentication_landing_have_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            // Inline emerald text link (Figma Link component 27:14).
            Text(
                text = stringResource(R.string.features_authentication_landing_sign_in),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.clickable(onClick = onSignIn),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun AuthLandingScreenPreview() {
    BlockTheme {
        BlockBackground {
            AuthLandingScreen()
        }
    }
}
