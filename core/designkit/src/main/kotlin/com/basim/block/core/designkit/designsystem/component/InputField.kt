package com.basim.block.core.designkit.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.designkit.designsystem.theme.LocalDimens

/**
 * Filled text field with the label stacked above the value inside a single bordered surface — the
 * design system's signature input. 60dp tall, `radiusMd` corners, `surface` fill on an
 * `outlineVariant` hairline. Mirrors the Figma `Input` component (node 29:87).
 */
@Composable
fun BlockInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailing: (@Composable () -> Unit)? = null,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = singleLine,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface,
        ),
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
        decorationBox = { innerTextField ->
            BlockFieldFrame(label = label, trailing = trailing) {
                Box {
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    innerTextField()
                }
            }
        },
    )
}

/**
 * The bordered filled shell shared by [BlockInputField] and [BlockPasswordField]: a 60dp row with a
 * stacked caption label above the field content, plus an optional trailing slot (eye toggle, etc.).
 */
@Composable
internal fun BlockFieldFrame(
    label: String,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val dimens = LocalDimens.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(dimens.radiusMd))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(dimens.radiusMd),
            )
            .padding(
                start = dimens.spacing20,
                end = if (trailing != null) dimens.spacing8 else dimens.spacing16
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dimens.spacing2),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            content()
        }
        trailing?.invoke()
    }
}

@PreviewLightDark
@Composable
private fun BlockInputFieldPreview() {
    BlockTheme {
        BlockBackground(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
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
