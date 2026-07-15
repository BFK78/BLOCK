package com.basim.block.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import com.basim.block.MainActivityUiState
import com.basim.block.core.navigation.EntryProvider

@Composable
fun rememberBlockAppState(
    uiState: MainActivityUiState,
    entryProviders: Set<EntryProvider>,
): BlockAppState {
    return remember(
        uiState.isUserAuthenticated,
        entryProviders,
        uiState.isOnBoardingCompleted
    ) {
        BlockAppState(
            appFlow  = if (uiState.isUserAuthenticated) AppFlow.MAIN_FLOW else AppFlow.AUTH_FLOW,
            entryProviders = entryProviders,
            isOnBoardingCompleted = uiState.isOnBoardingCompleted,
        )
    }
}

@Immutable
data class BlockAppState(
    val appFlow: AppFlow = AppFlow.AUTH_FLOW,
    val entryProviders: Set<EntryProvider>,
    val isOnBoardingCompleted: Boolean,
)

enum class AppFlow {
    AUTH_FLOW,
    MAIN_FLOW
}
