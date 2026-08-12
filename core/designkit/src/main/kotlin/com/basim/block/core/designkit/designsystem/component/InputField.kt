package com.basim.block.core.designkit.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.icon.BlockIcons
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.designkit.designsystem.theme.LocalDimens

internal val BlockFieldHeight = 60.dp
private val BlockFieldBorderWidth = 1.dp
private val BlockFieldErrorBorderWidth = 2.dp

/**
 * A text field is composed of three main parts:
 *
 * 1. BasicTextField (Engine) (Car Analogy)
 *    - Handles text editing, cursor, selection, focus, keyboard/IME,
 *      and all input-related behavior.
 *
 * 2. DecorationBox (Interior)
 *    - Controls how the text field is presented.
 *    - Responsible for laying out and styling elements such as the label,
 *      placeholder, leading/trailing icons, supporting text, error state,
 *      content padding, and the container.
 *
 * 3. Container (Body)
 *    - Defines the visual appearance of the text field itself.
 *    - Responsible for drawing the background, border, shape, shadow,
 *      focus indicator, and disabled/error appearance.
 */
@Composable
fun BlockInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailing: (@Composable () -> Unit)? = null,
) {
    val dimens = LocalDimens.current
    // Hoisted so the decoration box observes the same focus/press stream as the field itself.
    val interactionSource = remember { MutableInteractionSource() }

    val trailingIcon = trailing ?: if (isError) {
        { Icon(imageVector = BlockIcons.Error, contentDescription = null) }
    } else {
        null
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(dimens.spacing8),
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .height(BlockFieldHeight),
            enabled = enabled,
            singleLine = singleLine,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
            ),
            keyboardOptions = keyboardOptions,
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
                    singleLine = singleLine,
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    isError = isError,
                    label = { Text(text = label) },
                    placeholder = placeholder?.let { { Text(text = it) } },
                    trailingIcon = trailingIcon,
                    colors = blockFieldColors(),
                    contentPadding = blockFieldContentPadding(hasTrailing = trailingIcon != null),
                    container = { BlockFieldContainer(isError = isError) },
                )
            },
        )

        if (!supportingText.isNullOrEmpty()) {
            BlockFieldSupportingText(text = supportingText, isError = isError)
        }
    }
}

@Composable
internal fun BlockFieldContainer(isError: Boolean) {
    val dimens = LocalDimens.current
    val shape = RoundedCornerShape(dimens.radiusMd)

    val borderColor by animateColorAsState(
        targetValue = if (isError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.outlineVariant
        }
    )
    val borderWidth by animateDpAsState(
        targetValue = if (isError) BlockFieldErrorBorderWidth else BlockFieldBorderWidth,
    )
    Box(
        modifier = Modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(width = borderWidth, color = borderColor, shape = shape),
    )
}

@Composable
internal fun BlockFieldSupportingText(text: String, isError: Boolean) {
    val dimens = LocalDimens.current
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = if (isError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        modifier = Modifier.padding(start = dimens.spacing4),
    )
}

@Composable
internal fun blockFieldContentPadding(hasTrailing: Boolean): PaddingValues {
    val dimens = LocalDimens.current
    return TextFieldDefaults.contentPaddingWithLabel(
        start = dimens.spacing20,
        end = if (hasTrailing) dimens.spacing20 else dimens.spacing16,
        top = dimens.spacing8,
        bottom = dimens.spacing8,
    )
}

@Composable
internal fun blockFieldColors(): TextFieldColors = TextFieldDefaults.colors(
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    errorTextColor = MaterialTheme.colorScheme.onSurface,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    errorContainerColor = Color.Transparent,
    cursorColor = MaterialTheme.colorScheme.secondary,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
    focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
)

@PreviewLightDark
@Composable
private fun BlockInputFieldPreview() {
    BlockTheme {
        BlockBackground(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                BlockInputField(
                    value = "you@example.com",
                    onValueChange = {},
                    label = "Email",
                )
            }
        }
    }
}

/** Error: error-coloured outline at 2.dp, error label, trailing error icon, reason underneath. */
@PreviewLightDark
@Composable
private fun BlockInputFieldErrorPreview() {
    BlockTheme {
        BlockBackground(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                BlockInputField(
                    value = "you@example",
                    onValueChange = {},
                    label = "Email",
                    isError = true,
                    supportingText = "Enter a valid email address",
                )
            }
        }
    }
}

/** Empty + unfocused: the label fills the input area and the placeholder stays hidden. */
@PreviewLightDark
@Composable
private fun BlockInputFieldEmptyPreview() {
    BlockTheme {
        BlockBackground(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                BlockInputField(
                    value = "",
                    onValueChange = {},
                    label = "Email",
                    placeholder = "you@example.com",
                )
            }
        }
    }
}
