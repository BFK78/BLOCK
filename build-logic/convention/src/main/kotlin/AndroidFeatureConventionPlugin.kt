import com.android.build.api.dsl.LibraryExtension
import com.basim.block.configureGradleManagedDevices
import com.basim.block.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("block.android.library")
                apply("block.android.hilt")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner =
                        "androidx.test.runner.AndroidJUnitRunner"// Add custom when you start testing.
                }
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
            }
            dependencies {
                add("implementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                add("implementation", libs.findLibrary("androidx.tracing.ktx").get())

                "api"(project(":core:navigation"))
                "implementation"(project(":core:designkit"))

                add(
                    "androidTestImplementation",
                    libs.findLibrary("androidx.lifecycle.runtimeTesting").get()
                )
            }
        }
    }
}
