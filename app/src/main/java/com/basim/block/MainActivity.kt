package com.basim.block

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.navigation.EntryProvider
import com.basim.block.ui.BlockApp
import com.basim.block.ui.rememberBlockAppState
import com.basim.block.ui.splash.setCustomAnimation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var entryProviders: Set<@JvmSuppressWildcards EntryProvider>

    val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Must be installed before super.onCreate() so the system splash hands off cleanly.
        installSplashScreen().apply { setCustomAnimation() }
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val appState = rememberBlockAppState(
                uiState = uiState,
                entryProviders = entryProviders,
            )
            BlockTheme {
                BlockApp(
                    appState = appState
                )
            }
        }
    }
}
