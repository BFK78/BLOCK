package com.basim.block.core.designkit.designsystem.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.icon.BlockIcons
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.designkit.designsystem.theme.LocalDimens

private const val STRENGTH_SEGMENTS = 4

@Composable
fun BlockPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    helper: String? = null,
    showStrengthMeter: Boolean = false,
    strength: Int = 0,
    enabled: Boolean = true,
    isError: Boolean = false,
    placeholder: String? = null,
    toggleContentDescription: String? = null,
) {
    val dimens = LocalDimens.current
    var visible by remember { mutableStateOf(false) }
    // Hoisted so the decoration box observes the same focus/press stream as the field itself.
    val interactionSource = remember { MutableInteractionSource() }

    val visualTransformation = if (visible) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(dimens.spacing8),
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(BlockFieldHeight),
            enabled = enabled,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(
                if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.secondary
                },
            ),
            decorationBox = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    singleLine = true,
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    isError = isError,
                    label = { Text(text = label) },
                    placeholder = placeholder?.let { { Text(text = it) } },
                    trailingIcon = {
                        IconButton(
                            onClick = { visible = !visible },
                            modifier = Modifier.size(44.dp),
                        ) {
                            Icon(
                                imageVector = if (visible) BlockIcons.EyeOff else BlockIcons.Eye,
                                contentDescription = toggleContentDescription,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(22.dp),
                            )
                        }
                    },
                    colors = blockFieldColors(),
                    contentPadding = blockFieldContentPadding(hasTrailing = true),
                    container = { BlockFieldContainer(isError = isError) },
                )
            },
        )

        if (showStrengthMeter) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimens.spacing4),
                horizontalArrangement = Arrangement.spacedBy(6.dp), // Figma segment gap (no token)
            ) {
                repeat(STRENGTH_SEGMENTS) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                color = if (index < strength) {
                                    MaterialTheme.colorScheme.secondary
                                } else {
                                    MaterialTheme.colorScheme.surfaceContainerHigh
                                },
                                shape = RoundedCornerShape(dimens.radiusFull),
                            ),
                    )
                }
            }
        }

        if (!helper.isNullOrEmpty()) {
            BlockFieldSupportingText(text = helper, isError = isError)
        }
    }
}

@PreviewLightDark
@Composable
private fun BlockPasswordFieldPreview() {
    BlockTheme {
        BlockBackground(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                BlockPasswordField(
                    value = "supersecret",
                    onValueChange = {},
                    label = "Password",
                    helper = "At least 8 characters, one letter, one number or symbol.",
                    showStrengthMeter = true,
                    strength = 4,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun BlockPasswordFieldErrorPreview() {
    BlockTheme {
        BlockBackground(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                BlockPasswordField(
                    value = "secret",
                    onValueChange = {},
                    label = "Password",
                    helper = "Password must include at least 8 characters and one number.",
                    showStrengthMeter = true,
                    strength = 2,
                    isError = true,
                )
            }
        }
    }
}
