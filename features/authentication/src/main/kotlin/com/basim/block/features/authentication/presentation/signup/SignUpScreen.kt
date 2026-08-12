package com.basim.block.features.authentication.presentation.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
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
import com.basim.block.core.designkit.designsystem.component.BlockCheckbox
import com.basim.block.core.designkit.designsystem.component.BlockChip
import com.basim.block.core.designkit.designsystem.component.BlockInputField
import com.basim.block.core.designkit.designsystem.component.BlockLabeledDivider
import com.basim.block.core.designkit.designsystem.component.BlockPasswordField
import com.basim.block.core.designkit.designsystem.icon.BlockIcons
import com.basim.block.core.designkit.designsystem.style.rememberDefaultScreenStyle
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.designkit.designsystem.theme.LocalDimens
import com.basim.block.core.ui.animation.entrance
import com.basim.block.core.ui.animation.rememberEntranceTrigger
import com.basim.block.features.authentication.R
import com.basim.block.features.authentication.domain.validation.PasswordValidationError
import com.basim.block.features.authentication.domain.validation.PasswordValidationResult
import com.basim.block.features.authentication.presentation.common.components.AuthLinkFooter
import com.basim.block.features.authentication.presentation.common.components.AuthSocialSection
import com.basim.block.features.authentication.presentation.common.components.AuthTopAppBar

/**
 * Stateful entry point: owns the [SignUpViewModel], observes its state lifecycle-aware, and feeds
 * user input back through [SignUpViewModel.onAction]. Navigation actions stay hoisted to the caller.
 * The pixel UI lives in [SignUpScreen], kept stateless so its @Preview works without a ViewModel.
 */
@Composable
fun SignUpRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onAddPromo: () -> Unit = {},
    onGoogle: () -> Unit = {},
    onApple: () -> Unit = {},
    onSignIn: () -> Unit = {},
    onSignUpSucceeded: () -> Unit = {},
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Collect one-shot ViewModel → screen notifications exactly once for this composition.
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                SignUpUiEvent.SignUpSucceeded -> onSignUpSucceeded()
            }
        }
    }

    SignUpScreen(
        email = state.email,
        onEmailChange = { viewModel.onAction(SignUpUiAction.EmailChanged(it)) },
        password = state.password,
        onPasswordChange = { viewModel.onAction(SignUpUiAction.PasswordChanged(it)) },
        passwordValidation = state.passwordValidation,
        passwordStrength = passwordStrengthOf(state.passwordValidation),
        termsAccepted = state.termsAccepted,
        onTermsAcceptedChange = { viewModel.onAction(SignUpUiAction.TermsAcceptedChanged(it)) },
        onBack = onBack,
        onAddPromo = onAddPromo,
        onContinue = { viewModel.onAction(SignUpUiAction.SignUpClicked) },
        onGoogle = onGoogle,
        onApple = onApple,
        onSignIn = onSignIn,
        modifier = modifier,
    )
}

/** Meter score: one point per satisfied password rule. */
private fun passwordStrengthOf(validation: PasswordValidationResult): Int =
    PasswordValidationError.entries.size - validation.errors.size

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
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordValidation: PasswordValidationResult,
    passwordStrength: Int,
    termsAccepted: Boolean,
    onTermsAcceptedChange: (Boolean) -> Unit,
    onBack: () -> Unit,
    onAddPromo: () -> Unit,
    onContinue: () -> Unit,
    onGoogle: () -> Unit,
    onApple: () -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val passwordErrorText: String = buildPasswordErrorText(passwordValidation)


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
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(dimens.spacing12),
    ) {
        AuthTopAppBar(
            eyebrow = stringResource(R.string.features_authentication_signup_eyebrow),
            onBack = onBack,
            modifier = Modifier.entrance(visible, order = 0),
        )

        Text(
            text = stringResource(R.string.features_authentication_signup_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.entrance(visible, order = 1),
        )
        Text(
            text = stringResource(R.string.features_authentication_signup_subcopy),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.entrance(visible, order = 1),
        )

        BlockInputField(
            value = email,
            onValueChange = onEmailChange,
            label = stringResource(R.string.features_authentication_signup_email_label),
            placeholder = stringResource(R.string.features_authentication_signup_email_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .entrance(visible, order = 2)
                .focusRequester(emailFocusRequester),
        )

        BlockPasswordField(
            value = password,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.features_authentication_signup_password_label),
            helper = passwordErrorText,
            showStrengthMeter = true,
            strength = passwordStrength,
            modifier = Modifier.entrance(visible, order = 3),
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
            modifier = Modifier.entrance(visible, order = 4),
        )

        BlockCheckbox(
            checked = termsAccepted,
            onCheckedChange = onTermsAcceptedChange,
            label = stringResource(R.string.features_authentication_signup_terms),
            modifier = Modifier.entrance(visible, order = 5),
        )

        BlockButton(
            onClick = onContinue,
            shape = CircleShape, // filled pill CTA (Figma Button/Primary, radius-full)
            modifier = Modifier
                .entrance(visible, order = 6)
                .fillMaxWidth()
                .height(dimens.controlHeight),
        ) {
            Text(
                text = stringResource(R.string.features_authentication_signup_cta),
                style = MaterialTheme.typography.labelLarge,
            )
        }

        BlockLabeledDivider(
            label = stringResource(R.string.features_authentication_or),
            modifier = Modifier.entrance(visible, order = 7),
        )

        AuthSocialSection(
            onGoogle = onGoogle,
            onApple = onApple,
            modifier = Modifier.entrance(visible, order = 8),
        )

        AuthLinkFooter(
            prompt = stringResource(R.string.features_authentication_signup_footer_prompt),
            linkLabel = stringResource(R.string.features_authentication_signup_footer_link),
            onClick = onSignIn,
            // Last element in the cascade — its settle marks the whole intro as done.
            modifier = Modifier
                .entrance(visible, order = 9, onSettled = { introSettled = true })
                .padding(top = dimens.spacing4),
        )
    }
}

@Composable
private fun buildPasswordErrorText(passwordValidation: PasswordValidationResult): String {

    if (passwordValidation.isValid) return ""
    val missingList = mutableListOf<String>()

    if (PasswordValidationError.TOO_SHORT in passwordValidation.errors) {
        missingList.add(stringResource(R.string.features_authentication_password_too_short))
    }

    if (PasswordValidationError.MISSING_LOWER_CASE in passwordValidation.errors) {
        missingList.add(stringResource(R.string.features_authentication_password_missing_lowercase))
    }

    if (PasswordValidationError.MISSING_UPPERCASE in passwordValidation.errors) {
        missingList.add(stringResource(R.string.features_authentication_password_missing_uppercase))
    }

    if (PasswordValidationError.MISSING_DIGIT in passwordValidation.errors) {
        missingList.add(stringResource(R.string.features_authentication_password_number))
    }

    val errorText = StringBuilder()
    when (missingList.size) {
        1 -> {
            errorText.append(missingList.firstOrNull())
        }

        else -> {
            val last = missingList.removeAt(missingList.lastIndex)
            errorText.append("${missingList.joinToString(", ")} ${stringResource(R.string.features_authentication_and)} $last")
        }
    }

    return stringResource(R.string.features_authentication_password_helper, errorText.toString())
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
                passwordValidation = PasswordValidationResult(
                    errors = setOf(
                        PasswordValidationError.MISSING_DIGIT,
                        PasswordValidationError.MISSING_UPPERCASE
                    ),
                ),
                passwordStrength = 3,
                termsAccepted = terms,
                onTermsAcceptedChange = { terms = it },
                onBack = {},
                onAddPromo = {},
                onContinue = {},
                onGoogle = {},
                onApple = {},
                onSignIn = {},
            )
        }
    }
}
