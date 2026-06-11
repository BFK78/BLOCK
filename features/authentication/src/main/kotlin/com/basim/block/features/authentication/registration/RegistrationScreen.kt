package com.basim.block.features.authentication.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.component.BlockBackground
import com.basim.block.core.designkit.designsystem.component.BlockButton
import com.basim.block.core.designkit.designsystem.component.BlockIconButton
import com.basim.block.core.designkit.designsystem.component.BlockOutlinedButton
import com.basim.block.core.designkit.designsystem.component.BlockTextField
import com.basim.block.core.designkit.designsystem.icon.BlockIcons
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.features.authentication.common.components.AuthenticationInlineDivider

@Composable
fun RegistrationScreen(
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

        // stringResource(id = R.string.features_authentication_registration_title)
        Text(
            text = "Create your account",
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
                    contentDescription = "Google",
                    tint = Color.Unspecified
                )
            },
        ) {
            Text(text = "Continue with Google")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRegistrationScreen(
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = modifier
                    .padding(16.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                title = {},
                navigationIcon = {
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
                },
                actions = {
                    BlockOutlinedButton(
                        onClick = { /*TODO*/ },
                    ) {
                        Text(
                            text = "Sign in",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Set up your Profile ✍\uFE0F",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Create an account for blah blah blah and you can create the account here.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            BlockTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = ""
            ) { }

            Spacer(modifier = Modifier.height(16.dp))

            BlockTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = ""
            ) { }

            Spacer(modifier = Modifier.height(16.dp))

            BlockTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = ""
            ) { }

            Spacer(modifier = Modifier.height(16.dp))

            BlockTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = ""
            ) { }

            Spacer(modifier = Modifier.height(16.dp))

            BlockTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = ""
            ) { }

            Spacer(modifier = Modifier.weight(1f))

            BlockButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {}
            ) {
                Text(
                    text = "Continue",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@PreviewLightDark
@Preview
@Composable
private fun LoginScreenPreview() {
    BlockTheme {
        BlockBackground {
            NewRegistrationScreen()
        }
    }
}