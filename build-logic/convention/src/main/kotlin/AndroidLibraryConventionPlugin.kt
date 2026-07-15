import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.api.dsl.LibraryExtension
import com.basim.block.configureGradleManagedDevices
import com.basim.block.configureKotlinAndroid
import com.basim.block.configurePrintApksTask
import com.basim.block.disableUnnecessaryAndroidTests
import com.basim.block.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        with(target) {

            with(pluginManager) {
                apply("com.android.library")
                apply("block.android.lint")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                testOptions.animationsDisabled = true
//				configureFlavours(this) Implement When you need it.
                configureGradleManagedDevices(this)
                // The resource prefix is derived from the module name,
                // so resources inside ":core:module1" must be prefixed with "core_module1_"
                resourcePrefix = path.split("""\W""".toRegex()).drop(1).distinct()
                    .joinToString(separator = "_").lowercase() + "_"
                // The above code is the reason why it ask to write, feature_featureName_stringName in the strings.xml
            }

            extensions.configure<LibraryAndroidComponentsExtension> {
                configurePrintApksTask(this)
                disableUnnecessaryAndroidTests(target)
            }

            dependencies {
                // Pin the JUnit4 flavour: AGP 9's built-in Kotlin support no longer auto-detects
                // the test framework, so a bare kotlin("test") resolves without org.junit on the classpath.
                add("testImplementation", kotlin("test-junit"))
                add("implementation", libs.findLibrary("androidx.tracing.ktx").get())
            }
        }
    }
}