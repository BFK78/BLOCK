package com.basim.block.core.designkit.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.designkit.designsystem.theme.LocalDimens

internal val BlockFieldHeight = 60.dp

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
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailing: (@Composable () -> Unit)? = null,
) {
    // Hoisted so the decoration box observes the same focus/press stream as the field itself.
    val interactionSource = remember { MutableInteractionSource() }

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
        cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
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
                trailingIcon = trailing,
                colors = blockFieldColors(),
                contentPadding = blockFieldContentPadding(hasTrailing = trailing != null),
                container = { BlockFieldContainer() },
            )
        },
    )
}

@Composable
internal fun BlockFieldContainer() {
    val dimens = LocalDimens.current
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(dimens.radiusMd))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(dimens.radiusMd),
            ),
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
