package com.basim.block.core.designkit.designsystem.style

import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.contentPadding
import androidx.compose.foundation.style.fillSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.basim.block.core.designkit.designsystem.theme.LocalDimens

/**
val defaultScreenStyle = Style {
//    val dimens = LocalDimens.current
// background Add background after Style api become compatible with the material theming.
fillSize()
contentPadding(horizontal = 24.dp, vertical = 32.dp) // TODO: Replace it with dimens
}
 */

@Composable
fun rememberDefaultScreenStyle(): Style {

    val dimens = LocalDimens.current
    val backgroundColor = MaterialTheme.colorScheme.background

    return remember(dimens, backgroundColor) {
        Style {
            fillSize()
            background(backgroundColor)
            contentPadding(horizontal = dimens.spacing24, vertical = dimens.spacing32)
        }
    }
}