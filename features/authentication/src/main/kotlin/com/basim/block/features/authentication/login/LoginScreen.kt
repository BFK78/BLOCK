package com.basim.block.features.authentication.login

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
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
import com.basim.block.core.designkit.designsystem.component.BlockInputField
import com.basim.block.core.designkit.designsystem.component.BlockPasswordField
import com.basim.block.core.designkit.designsystem.component.BlockTextLink
import com.basim.block.core.designkit.designsystem.component.BlockTopAppBar
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.designkit.designsystem.theme.LocalDimens
import com.basim.block.features.authentication.R
import com.basim.block.features.authentication.common.components.AuthLinkFooter
import com.basim.block.features.authentication.common.components.AuthSocialSection

// Figma column gap is 14px between items — no spacing token covers 14, so this is an intentional literal.
private val LOGIN_ITEM_GAP = 14.dp

/**
 * Login — pixel-built from Figma. Sign-in form in the auth flow: top bar, serif headline + subcopy,
 * email + password fields, a right-aligned "Forgot password?" link, a primary "Sign in" CTA, then
 * the shared social-auth block and a "Create an account" footer pinned toward the bottom. Stateless —
 * field values are hoisted for later ViewModel wiring.
 *
 * Figma: https://www.figma.com/design/R1bw3ysZmoZ83l0VCfUTCt/?node-id=46-164
 * Page: Auth Flows (2:3)  ·  Mode: Dark (canonical) — binds to M3 roles, so it also serves Light.
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    email: String = "",
    onEmailChange: (String) -> Unit = {},
    password: String = "",
    onPasswordChange: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onSignIn: () -> Unit = {},
    onGoogle: () -> Unit = {},
    onApple: () -> Unit = {},
    onCreateAccount: () -> Unit = {},
) {
    val dimens = LocalDimens.current
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = dimens.spacing24, vertical = dimens.spacing32),
        verticalArrangement = Arrangement.spacedBy(LOGIN_ITEM_GAP),
    ) {
        BlockTopAppBar(
            eyebrow = stringResource(R.string.features_authentication_login_eyebrow),
            onBack = onBack,
        )

        Text(
            text = stringResource(R.string.features_authentication_login_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(R.string.features_authentication_login_subcopy),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        BlockInputField(
            value = email,
            onValueChange = onEmailChange,
            label = stringResource(R.string.features_authentication_login_email_label),
            placeholder = stringResource(R.string.features_authentication_login_email_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        BlockPasswordField(
            value = password,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.features_authentication_login_password_label),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            BlockTextLink(
                text = stringResource(R.string.features_authentication_forgot_password),
                onClick = onForgotPassword,
            )
        }

        BlockButton(
            onClick = onSignIn,
            shape = CircleShape, // filled pill CTA (Figma Button/Primary, radius-full)
            modifier = Modifier
                .fillMaxWidth()
                .height(dimens.controlHeight),
        ) {
            Text(
                text = stringResource(R.string.features_authentication_login_cta),
                style = MaterialTheme.typography.labelLarge,
            )
        }

        AuthSocialSection(
            onGoogle = onGoogle,
            onApple = onApple,
            verticalSpacing = LOGIN_ITEM_GAP,
        )

        Spacer(modifier = Modifier.weight(1f))

        AuthLinkFooter(
            prompt = stringResource(R.string.features_authentication_login_footer_prompt),
            linkLabel = stringResource(R.string.features_authentication_login_footer_link),
            onClick = onCreateAccount,
        )
    }
}

@PreviewLightDark
@Composable
private fun LoginScreenPreview() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("supersecret") }
    BlockTheme {
        BlockBackground {
            LoginScreen(
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
            )
        }
    }
}
