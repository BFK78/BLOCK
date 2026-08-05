plugins {
    alias(libs.plugins.block.android.feature)
    alias(libs.plugins.block.android.library.compose)
}

android {
    namespace = "com.basim.block.features.authentication"
}

dependencies {
    implementation(project(":core:common"))
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
}