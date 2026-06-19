package com.basim.block.core.designkit.designsystem.component

import androidx.compose.foundation.background
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

/** Number of segments in the password strength meter. */
private const val STRENGTH_SEGMENTS = 4

/**
 * Password field: the shared [BlockFieldFrame] shell with a trailing show/hide eye toggle, plus an
 * optional 4-segment strength meter and helper line below. Visibility is managed internally; the
 * value itself is hoisted. Mirrors the Figma `Input/Password` component (node 31:96).
 *
 * @param strength number of filled strength segments (0..4); only shown when [showStrengthMeter].
 */
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
    placeholder: String? = null,
    toggleContentDescription: String? = null,
) {
    val dimens = LocalDimens.current
    var visible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimens.spacing8),
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (visible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
            decorationBox = { innerTextField ->
                BlockFieldFrame(
                    label = label,
                    trailing = {
                        IconButton(
                            onClick = { visible = !visible },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = if (visible) BlockIcons.EyeOff else BlockIcons.Eye,
                                contentDescription = toggleContentDescription,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(22.dp),
                            )
                        }
                    },
                ) {
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

        if (helper != null) {
            Text(
                text = helper,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = dimens.spacing4),
            )
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
