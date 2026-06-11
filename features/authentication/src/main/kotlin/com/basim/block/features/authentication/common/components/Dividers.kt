package com.basim.block.features.authentication.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.component.BlockBackground
import com.basim.block.core.designkit.designsystem.theme.BlockTheme

@Composable
fun AuthenticationInlineDivider(
    modifier: Modifier = Modifier,
    text: String = "OR" // Replace with the strings from the strings.xml
) {
    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
        )

        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Preview
@Composable
private fun PreviewAuthenticationInlineDivider() {
    BlockTheme {
        BlockBackground {
            AuthenticationInlineDivider()
        }
    }
}