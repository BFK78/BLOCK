package com.basim.block.core.designkit.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.theme.BlockTheme


/**
 * Not adding color, add when you need it.
 * */

@Composable
fun BlockTextField(
    modifier: Modifier = Modifier,
    value: String,
    enabled: Boolean = true,
    isError: Boolean = false,
    cornerRadius: Dp = 8.dp,
    placeholder: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        label?.let {
            it()
        }

        OutlinedTextField(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = true,
            maxLines = 1,
            isError = isError,
            placeholder = placeholder,
            shape = RoundedCornerShape(cornerRadius),
            colors = OutlinedTextFieldDefaults.colors()
        )
    }
}

@PreviewLightDark
@Composable
fun BlockTextFieldPreview() {
    BlockTheme {
        BlockBackground(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        ) {
            BlockTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Email") }
            )
        }
    }
}