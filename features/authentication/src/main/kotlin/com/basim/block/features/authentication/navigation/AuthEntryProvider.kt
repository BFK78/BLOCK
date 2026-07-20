package com.basim.block.features.authentication.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.basim.block.core.navigation.EntryProvider
import com.basim.block.core.navigation.EntryProviderType
import com.basim.block.core.navigation.Navigator
import com.basim.block.features.authentication.presentation.landing.AuthLandingScreen
import com.basim.block.features.authentication.presentation.login.LoginScreen
import com.basim.block.features.authentication.presentation.onboarding.OnboardingScreen
import com.basim.block.features.authentication.presentation.signup.SignUpScreen
import javax.inject.Inject

class AuthEntryProvider @Inject constructor(): EntryProvider {

    override fun register(
        scope: EntryProviderScope<NavKey>,
        navigator: Navigator
    ) = with(scope) {
        entry<AuthRoute.OnBoarding> {
            OnboardingScreen(
                onSkip = {
                    navigator.goTo(AuthRoute.AuthLanding)
                },
                onFinish = {
                    navigator.goTo(AuthRoute.AuthLanding)
                }
            )
        }

        entry<AuthRoute.AuthLanding> {
            AuthLandingScreen(
                onCreateProfile = {
                    navigator.goTo(AuthRoute.Register)
                },
                onSignIn = {
                    navigator.goTo(AuthRoute.Login)
                }
            )
        }

        entry<AuthRoute.Login> {
            LoginScreen(
                onCreateAccount = {
                    navigator.goTo(AuthRoute.Register)
                }
            )
        }

        entry<AuthRoute.Register> {
            SignUpScreen(
                onSignIn = {
                    navigator.goTo(AuthRoute.Login)
                }
            )
        }
    }

    override fun getType() = EntryProviderType.AUTH_ENTRY_PROVIDER
}