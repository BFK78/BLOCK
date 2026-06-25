package com.basim.block.features.authentication.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.basim.block.core.designkit.designsystem.component.BlockBackground
import com.basim.block.core.designkit.designsystem.component.BlockButton
import com.basim.block.core.designkit.designsystem.component.BlockInputField
import com.basim.block.core.designkit.designsystem.component.BlockLabeledDivider
import com.basim.block.core.designkit.designsystem.component.BlockPasswordField
import com.basim.block.core.designkit.designsystem.component.BlockTextLink
import com.basim.block.core.designkit.designsystem.style.rememberDefaultScreenStyle
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.designkit.designsystem.theme.LocalDimens
import com.basim.block.features.authentication.R
import com.basim.block.features.authentication.presentation.common.components.AuthLinkFooter
import com.basim.block.features.authentication.presentation.common.components.AuthSocialSection
import com.basim.block.features.authentication.presentation.common.components.AuthTopAppBar

// Figma column gap is 14px between items — no spacing token covers 14, so this is an intentional literal.
private val LOGIN_ITEM_GAP = 14.dp

/**
 * Stateful entry point: owns the [LoginViewModel], observes its state lifecycle-aware, and feeds
 * field edits back through [LoginViewModel.onAction]. Navigation actions stay hoisted to the caller —
 * there is no nav graph wired yet, so they default to no-ops. The pixel UI lives in [LoginScreen],
 * kept stateless so its @Preview works without a ViewModel.
 */
@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onSignIn: () -> Unit = {},
    onGoogle: () -> Unit = {},
    onApple: () -> Unit = {},
    onCreateAccount: () -> Unit = {},
    viewModel: LoginViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Collect one-shot ViewModel → screen notifications exactly once for this composition.
    LaunchedEffect(Unit) {
        viewModel.events.collect {
            // when (it) { } — handle ViewModel → screen notifications here (none yet)
        }
    }

    LoginScreen(
        modifier = modifier,
        email = state.email,
        onEmailChange = { viewModel.onAction(LoginUiAction.EmailChanged(it)) },
        password = state.password,
        onPasswordChange = { viewModel.onAction(LoginUiAction.PasswordChanged(it)) },
        onBack = onBack,
        onForgotPassword = onForgotPassword,
        onSignIn = onSignIn,
        onGoogle = onGoogle,
        onApple = onApple,
        onCreateAccount = onCreateAccount,
    )
}

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
            .styleable(null, rememberDefaultScreenStyle())
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(LOGIN_ITEM_GAP),
    ) {
        AuthTopAppBar(
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

        BlockLabeledDivider(label = stringResource(R.string.features_authentication_or))

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