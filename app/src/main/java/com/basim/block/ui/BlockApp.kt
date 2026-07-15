package com.basim.block.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.basim.block.ui.auth.AuthNavDisplay

@Composable
fun BlockApp(
    appState: BlockAppState,
    modifier: Modifier = Modifier
) {
    when (appState.appFlow) {
        AppFlow.AUTH_FLOW -> {
            AuthNavDisplay(
                appState = appState,
                modifier = modifier
            )
        }
        AppFlow.MAIN_FLOW -> {

        }
    }
}