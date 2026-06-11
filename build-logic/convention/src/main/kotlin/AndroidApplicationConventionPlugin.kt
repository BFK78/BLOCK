import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.basim.block.configureGradleManagedDevices
import com.basim.block.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                // TODO Find a way to add this directly from version catalog (libs), eg: libs.plugins.androidApplication or any other way
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("block.android.lint")
                apply("com.dropbox.dependency-guard") // Very useful plugin for dependency management, need to use this in our every project.
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 36
                @Suppress("UnstableApiUsage")
                testOptions.animationsDisabled = false
                configureGradleManagedDevices(this)
            }

            extensions.configure<ApplicationAndroidComponentsExtension> {
//				configurePrintApksTask(this)
//				configureBadgingTasks(extensions.getByType<BaseExtension>(), this) // Not yet created, create when you start to test.
            }
        }
    }
}