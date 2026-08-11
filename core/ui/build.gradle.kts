plugins {
    alias(libs.plugins.block.android.library)
    alias(libs.plugins.block.android.library.compose)
}

android {
    namespace = "com.basim.block.core.ui"
}

dependencies {
    implementation(libs.androidx.compose.animation.core)
}
