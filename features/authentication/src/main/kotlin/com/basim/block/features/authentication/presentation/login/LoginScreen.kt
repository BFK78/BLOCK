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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.basim.block.core.designkit.designsystem.component.BlockBackground
import com.basim.block.core.designkit.designsystem.component.BlockButton
import com.basim.block.core.designkit.designsystem.component.BlockInputField
import com.basim.block.core.designkit.designsystem.component.BlockLabeledDivider
import com.basim.block.core.designkit.designsystem.component.BlockPasswordField
import com.basim.block.core.designkit.designsystem.component.BlockTextLink
import com.basim.block.core.designkit.designsystem.style.rememberDefaultScreenStyle
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.designkit.designsystem.theme.LocalDimens
import com.basim.block.core.ui.animation.entrance
import com.basim.block.core.ui.animation.rememberEntranceTrigger
import com.basim.block.features.authentication.R
import com.basim.block.features.authentication.presentation.common.components.AuthLinkFooter
import com.basim.block.features.authentication.presentation.common.components.AuthSocialSection
import com.basim.block.features.authentication.presentation.common.components.AuthTopAppBar

// Figma column gap is 14px between items — no spacing token covers 14, so this is an intentional literal.
private val LOGIN_ITEM_GAP = 14.dp

/**
 * Stateful entry point: owns the [LoginViewModel], observes its state lifecycle-aware, and feeds
 * field edits back through [LoginViewModel.onAction]. Navigation actions stay hoisted to the caller.
 * The pixel UI lives in [LoginScreen], kept stateless so its @Preview works without a ViewModel.
 */
@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onGoogle: () -> Unit = {},
    onApple: () -> Unit = {},
    onCreateAccount: () -> Unit = {},
    onSignInSucceeded: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Collect one-shot ViewModel → screen notifications exactly once for this composition.
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                LoginUiEvent.SignInSucceeded -> onSignInSucceeded()
            }
        }
    }

    LoginScreen(
        email = state.email,
        onEmailChange = { viewModel.onAction(LoginUiAction.EmailChanged(it)) },
        password = state.password,
        onPasswordChange = { viewModel.onAction(LoginUiAction.PasswordChanged(it)) },
        onBack = onBack,
        onForgotPassword = onForgotPassword,
        onSignIn = { viewModel.onAction(LoginUiAction.SignInClicked) },
        onGoogle = onGoogle,
        onApple = onApple,
        onCreateAccount = onCreateAccount,
        modifier = modifier,
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
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onBack: () -> Unit,
    onForgotPassword: () -> Unit,
    onSignIn: () -> Unit,
    onGoogle: () -> Unit,
    onApple: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = LocalDimens.current
    // Flips true after first composition, driving the staggered fade+rise intro below.
    val visible = rememberEntranceTrigger()
    // Autofocus the email field, but only once the intro has fully settled — requesting focus
    // earlier would raise the IME mid-animation and resize the layout (reads as jank).
    val emailFocusRequester = remember { FocusRequester() }
    var introSettled by remember { mutableStateOf(false) }
    LaunchedEffect(introSettled) {
        if (introSettled) emailFocusRequester.requestFocus()
    }
    Column(
        modifier = modifier
            .styleable(null, rememberDefaultScreenStyle())
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(LOGIN_ITEM_GAP),
    ) {
        AuthTopAppBar(
            eyebrow = stringResource(R.string.features_authentication_login_eyebrow),
            onBack = onBack,
            modifier = Modifier.entrance(visible, order = 0),
        )

        Text(
            text = stringResource(R.string.features_authentication_login_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.entrance(visible, order = 1),
        )
        Text(
            text = stringResource(R.string.features_authentication_login_subcopy),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.entrance(visible, order = 1),
        )

        BlockInputField(
            value = email,
            onValueChange = onEmailChange,
            label = stringResource(R.string.features_authentication_login_email_label),
            placeholder = stringResource(R.string.features_authentication_login_email_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .entrance(visible, order = 2)
                .focusRequester(emailFocusRequester),
        )

        BlockPasswordField(
            value = password,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.features_authentication_login_password_label),
            modifier = Modifier.entrance(visible, order = 3),
        )

        Row(
            modifier = Modifier
                .entrance(visible, order = 4)
                .fillMaxWidth(),
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
                .entrance(visible, order = 5)
                .fillMaxWidth()
                .height(dimens.controlHeight),
        ) {
            Text(
                text = stringResource(R.string.features_authentication_login_cta),
                style = MaterialTheme.typography.labelLarge,
            )
        }

        BlockLabeledDivider(
            label = stringResource(R.string.features_authentication_or),
            modifier = Modifier.entrance(visible, order = 6),
        )

        AuthSocialSection(
            onGoogle = onGoogle,
            onApple = onApple,
            verticalSpacing = LOGIN_ITEM_GAP,
            modifier = Modifier.entrance(visible, order = 7),
        )

        Spacer(modifier = Modifier.weight(1f))

        AuthLinkFooter(
            prompt = stringResource(R.string.features_authentication_login_footer_prompt),
            linkLabel = stringResource(R.string.features_authentication_login_footer_link),
            onClick = onCreateAccount,
            // Last element in the cascade — its settle marks the whole intro as done.
            modifier = Modifier.entrance(visible, order = 8, onSettled = { introSettled = true }),
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
                onBack = {},
                onForgotPassword = {},
                onSignIn = {},
                onGoogle = {},
                onApple = {},
                onCreateAccount = {},
            )
        }
    }
}