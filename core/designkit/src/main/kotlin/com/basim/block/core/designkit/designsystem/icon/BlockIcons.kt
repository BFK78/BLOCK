package com.basim.block.core.designkit.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.basim.block.core.designsystem.R

object BlockIcons {

    val GoogleIcon: ImageVector
        @Composable get() = ImageVector.vectorResource(id = R.drawable.ic_google)
    val ArrowBack = Icons.AutoMirrored.Rounded.ArrowBack

}