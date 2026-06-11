plugins {
    alias(libs.plugins.block.android.feature)
    alias(libs.plugins.block.android.library.compose)
}

android {
    namespace = "com.basim.block.features.authentication"
}

//TODO : HOW TO GET THE PROJECTS REFERENCE IN GRADLE
dependencies {
    implementation(project(":core:designkit"))
}