plugins {
    alias(libs.plugins.block.android.library)
    alias(libs.plugins.block.android.library.compose)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.basim.block.core.designsystem"
}

dependencies {

    // API vs IMPLEMENTATION => API is used when you want to access the internal library, that is whatever the library uses you can also use that, but implementation will not allow you to do that.
    // Better example here is that whoever implement this module can access this dependency declared
    // in api, and if you use implementation the dependency will only be visible to the module itself.
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.util)

    implementation(libs.coil.kt.compose)
    implementation(libs.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}