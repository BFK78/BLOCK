plugins {
    alias(libs.plugins.block.android.library)
    alias(libs.plugins.block.android.library.compose)
}

android {
    namespace = "com.basim.block.core.navigation"
}

dependencies {
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
}