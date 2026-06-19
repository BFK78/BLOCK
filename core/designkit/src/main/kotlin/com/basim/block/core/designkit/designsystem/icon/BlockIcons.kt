package com.basim.block.core.designkit.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.basim.block.core.designsystem.R

object BlockIcons {

    val GoogleIcon: ImageVector
        @Composable get() = ImageVector.vectorResource(id = R.drawable.core_designkit_ic_google)
    val AppleIcon: ImageVector
        @Composable get() = ImageVector.vectorResource(id = R.drawable.core_designkit_ic_apple)
    val Eye: ImageVector
        @Composable get() = ImageVector.vectorResource(id = R.drawable.core_designkit_ic_eye)
    val EyeOff: ImageVector
        @Composable get() = ImageVector.vectorResource(id = R.drawable.core_designkit_ic_eye_off)
    val Plus: ImageVector
        @Composable get() = ImageVector.vectorResource(id = R.drawable.core_designkit_ic_plus)
    val ArrowBack = Icons.AutoMirrored.Rounded.ArrowBack

}