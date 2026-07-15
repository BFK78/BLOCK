plugins {
    alias(libs.plugins.block.android.library)
    alias(libs.plugins.block.android.library.compose)
    alias(libs.plugins.block.android.hilt)
}

android {
    namespace = "com.basim.block.core.navigation"
}

dependencies {
    api(libs.androidx.navigation3.runtime)
    api(libs.androidx.navigation3.ui)
}