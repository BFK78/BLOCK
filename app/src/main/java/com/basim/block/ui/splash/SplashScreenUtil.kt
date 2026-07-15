package com.basim.block.ui.splash

import androidx.core.splashscreen.SplashScreen

fun SplashScreen.setCustomAnimation() {
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