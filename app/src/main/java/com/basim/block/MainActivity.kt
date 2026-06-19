package com.basim.block

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.features.authentication.presentation.landing.AuthLandingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Must be installed before super.onCreate() so the system splash hands off cleanly.
        installSplashScreen().apply {
            setOnExitAnimationListener { splashView ->
                splashView.iconView.postDelayed(
                    {
                        splashView.iconView.animate()
                            .scaleX(1.5f)
                            .scaleY(1.5f)
                            .alpha(0f)
                            .setDuration(400)
                            .withEndAction {
                                splashView.view.animate()
                                    .alpha(0f)
                                    .setDuration(150)
                                    .withEndAction {
                                        splashView.remove()
                                    }
                                    .start()
                            }
                            .start()
                    }, 250 // Adding a small delay to gracefully complete the icon animation.
                )
            }
        }

        super.onCreate(savedInstanceState)
        setContent {
            BlockTheme {
                AuthLandingScreen { }
            }
        }
    }
}
