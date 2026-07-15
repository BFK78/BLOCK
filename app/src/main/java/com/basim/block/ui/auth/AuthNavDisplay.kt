package com.basim.block.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.basim.block.core.navigation.EntryProviderType
import com.basim.block.core.navigation.auth.AuthNavigator
import com.basim.block.features.authentication.navigation.AuthRoute
import com.basim.block.ui.BlockAppState

@Composable
fun AuthNavDisplay(
    appState: BlockAppState,
    modifier: Modifier = Modifier
) {

    val navigator = remember {
        AuthNavigator(
            if (appState.isOnBoardingCompleted)
                AuthRoute.AuthLanding
            else
                AuthRoute.OnBoarding
        )
    }
    val authEntryProvider = appState.entryProviders.firstOrNull {
        it.getType() == EntryProviderType.AUTH_ENTRY_PROVIDER
    }

    NavDisplay(
        modifier = modifier,
        backStack = navigator.backStack,
        onBack = { navigator.goBack() },
        entryProvider = entryProvider(
            fallback = { key ->
                NavEntry(key) { error("No NavEntry registered for $key")}
            }
        ) {
            authEntryProvider?.register(
                scope = this,
                navigator = navigator
            )
        }
    )
}