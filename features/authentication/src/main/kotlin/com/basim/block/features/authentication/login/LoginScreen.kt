package com.basim.block.features.authentication.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.component.BlockBackground
import com.basim.block.core.designkit.designsystem.component.BlockButton
import com.basim.block.core.designkit.designsystem.component.BlockIconButton
import com.basim.block.core.designkit.designsystem.component.BlockTextField
import com.basim.block.core.designkit.designsystem.icon.BlockIcons
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.features.authentication.R
import com.basim.block.features.authentication.common.components.AuthenticationInlineDivider

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {

        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Icon(
                imageVector = BlockIcons.ArrowBack,
                contentDescription = "Back navigation",
            )
        }

        Spacer(modifier = Modifier)

        Text(
            text = stringResource(id = R.string.features_authentication_login_title),
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier)

        BlockTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Email")
            },
            placeholder = {
                Text(text = "abc@sample.com")
            },
            value = ""
        ) {

        }

        BlockTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Password")
            },
            placeholder = {
                Text(text = "********")
            },
            value = ""
        ) {

        }

        Spacer(modifier = Modifier)

        Text(
            text = "Forgot Password?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier)

        BlockButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier)

        AuthenticationInlineDivider()

        Spacer(modifier = Modifier)

        BlockIconButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /*TODO*/ },
            leadingIcon = {
                Icon(
                    imageVector = BlockIcons.GoogleIcon,
                    contentDescription = "Sign in with google",
                    tint = Color.Unspecified
                )
            },
        ) {
            Text(text = "Continue with Google")
        }
    }
}

@Composable
fun NewLoginScreen(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

        }

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            elevation = CardDefaults.outlinedCardElevation(
                defaultElevation = 4.dp,
                focusedElevation = 8.dp
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp
            ),
            colors = CardDefaults
                .outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
        ) {

            // using another column for giving padding.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 32.dp,
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 24.dp
                    )
            ) {

                Text(
                    text = stringResource(R.string.features_authentication_login_title),
                    style = MaterialTheme.typography.titleLarge,
                )

                Spacer(modifier = Modifier.height(16.dp))

                BlockTextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Email or Username or Phone Number"
                        )
                    },
                    value = ""
                ) {

                }

                Spacer(modifier = Modifier.height(16.dp))

                BlockTextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = "********")
                    },
                    value = ""
                ) {

                }

                Spacer(modifier = Modifier.height(16.dp))

                BlockButton(
                    onClick = { /*TODO*/ },
                    contentPadding = PaddingValues(
                        16.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Sign in")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Forgot Password?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.weight(1f))

                val text = buildAnnotatedString {
                    append("Don't have an account? ")
                    pushStringAnnotation(tag = "signup", annotation = "signup")
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Sign up")
                    }
                    pop()
                }

                ClickableText(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Center,
                    ),
                    onClick = { offset ->
                        text.getStringAnnotations(tag = "signup", start = offset, end = offset)
                            .firstOrNull()?.let {
                            }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewNewLoginScreen() {
    BlockTheme {
        BlockBackground {
            NewLoginScreen()
        }
    }
}