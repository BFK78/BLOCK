package com.basim.block.features.authentication.presentation.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.component.BlockBackground
import com.basim.block.core.designkit.designsystem.component.BlockButton
import com.basim.block.core.designkit.designsystem.component.BlockCheckbox
import com.basim.block.core.designkit.designsystem.component.BlockChip
import com.basim.block.core.designkit.designsystem.component.BlockInputField
import com.basim.block.core.designkit.designsystem.component.BlockPasswordField
import com.basim.block.core.designkit.designsystem.component.BlockTopAppBar
import com.basim.block.core.designkit.designsystem.icon.BlockIcons
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.designkit.designsystem.theme.LocalDimens
import com.basim.block.features.authentication.R
import com.basim.block.features.authentication.presentation.common.components.AuthLinkFooter
import com.basim.block.features.authentication.presentation.common.components.AuthSocialSection

/**
 * Sign Up — pixel-built from Figma. Account-creation form in the auth flow: top bar, serif headline
 * + subcopy, email + password (with strength meter) fields, a promo-code chip, a terms checkbox, a
 * primary "Continue" CTA, then the shared social-auth block and a "Sign in" footer. Stateless — the
 * field values and terms toggle are hoisted for later ViewModel wiring.
 *
 * Figma: https://www.figma.com/design/R1bw3ysZmoZ83l0VCfUTCt/?node-id=43-42
 * Page: Auth Flows (2:3)  ·  Mode: Dark (canonical) — binds to M3 roles, so it also serves Light.
 */
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    email: String = "",
    onEmailChange: (String) -> Unit = {},
    password: String = "",
    onPasswordChange: (String) -> Unit = {},
    passwordStrength: Int = 0,
    termsAccepted: Boolean = false,
    onTermsAcceptedChange: (Boolean) -> Unit = {},
    onBack: () -> Unit = {},
    onAddPromo: () -> Unit = {},
    onContinue: () -> Unit = {},
    onGoogle: () -> Unit = {},
    onApple: () -> Unit = {},
    onSignIn: () -> Unit = {},
) {
    val dimens = LocalDimens.current
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = dimens.spacing24, vertical = dimens.spacing32),
        verticalArrangement = Arrangement.spacedBy(dimens.spacing12),
    ) {
        BlockTopAppBar(
            eyebrow = stringResource(R.string.features_authentication_signup_eyebrow),
            onBack = onBack,
        )

        Text(
            text = stringResource(R.string.features_authentication_signup_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(R.string.features_authentication_signup_subcopy),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        BlockInputField(
            value = email,
            onValueChange = onEmailChange,
            label = stringResource(R.string.features_authentication_signup_email_label),
            placeholder = stringResource(R.string.features_authentication_signup_email_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        BlockPasswordField(
            value = password,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.features_authentication_signup_password_label),
            helper = stringResource(R.string.features_authentication_password_helper),
            showStrengthMeter = true,
            strength = passwordStrength,
        )

        BlockChip(
            label = stringResource(R.string.features_authentication_signup_promo),
            onClick = onAddPromo,
            leadingIcon = {
                Icon(
                    imageVector = BlockIcons.Plus,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp),
                )
            },
        )

        BlockCheckbox(
            checked = termsAccepted,
            onCheckedChange = onTermsAcceptedChange,
            label = stringResource(R.string.features_authentication_signup_terms),
        )

        BlockButton(
            onClick = onContinue,
            shape = CircleShape, // filled pill CTA (Figma Button/Primary, radius-full)
            modifier = Modifier
                .fillMaxWidth()
                .height(dimens.controlHeight),
        ) {
            Text(
                text = stringResource(R.string.features_authentication_signup_cta),
                style = MaterialTheme.typography.labelLarge,
            )
        }

        AuthSocialSection(onGoogle = onGoogle, onApple = onApple)

        AuthLinkFooter(
            prompt = stringResource(R.string.features_authentication_signup_footer_prompt),
            linkLabel = stringResource(R.string.features_authentication_signup_footer_link),
            onClick = onSignIn,
            modifier = Modifier.padding(top = dimens.spacing4),
        )
    }
}

@PreviewLightDark
@Composable
private fun SignUpScreenPreview() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("supersecret") }
    var terms by remember { mutableStateOf(false) }
    BlockTheme {
        BlockBackground {
            SignUpScreen(
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                passwordStrength = 3,
                termsAccepted = terms,
                onTermsAcceptedChange = { terms = it },
            )
        }
    }
}
