package com.basim.block.features.authentication.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class AuthRoute: NavKey {
    @Serializable
    object OnBoarding: AuthRoute()

    @Serializable
    object AuthLanding: AuthRoute()

    @Serializable
    object Login: AuthRoute()

    @Serializable
    object Register: AuthRoute()
}