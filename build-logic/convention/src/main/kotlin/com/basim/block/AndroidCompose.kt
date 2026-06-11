package com.basim.block

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {

    commonExtension.apply {

        buildFeatures {
            compose = true
        }

        // composeOptions.kotlinCompilerExtensionVersion is no longer needed with Kotlin 2.0+
        // The Compose compiler is now bundled with the Kotlin plugin (org.jetbrains.kotlin.plugin.compose)

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            add("androidTestImplementation", platform(bom))
            add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
        }

        testOptions {
            unitTests {
                // This is for Robolectric
                isIncludeAndroidResources = true
            }
        }

        // Adding arguments for the kotlin compiler for capturing the metrics and reports, so that we can analyze the performance.
        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions {
                freeCompilerArgs.addAll(buildComposeMetricsParameters())
                freeCompilerArgs.addAll(stabilityConfiguration())
                // experimentalStrongSkipping is enabled by default in Kotlin 2.0+ compose compiler
            }
        }
    }
}


/**
 * To debug the stability of your composables, run the task as follows:
 * `./gradlew assembleRelease -PcomposeCompilerReports=true`
 * */
private fun Project.buildComposeMetricsParameters(): List<String> {

    val metricParameters = mutableListOf<String>()
    val enableMetricsProvider = project.providers.gradleProperty("enableComposeCompilerMetrics")
    val relativePath = projectDir.relativeTo(rootDir)
    val buildDir = layout.buildDirectory.get().asFile
    val enableMetrics = (enableMetricsProvider.orNull == "true")
    if (enableMetrics) {
        val metricsFolder = buildDir.resolve("compose-metrics").resolve(relativePath)
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + metricsFolder.absolutePath,
        )
    }

    val enableReportsProvider = project.providers.gradleProperty("enableComposeCompilerReports")
    val enableReports = (enableReportsProvider.orNull == "true")
    if (enableReports) {
        val reportsFolder = buildDir.resolve("compose-reports").resolve(relativePath)
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + reportsFolder.absolutePath
        )
    }

    return metricParameters.toList()
}

// https://developer.android.com/develop/ui/compose/performance/stability/fix#configuration-file
// Read this if you have any doubt, basically we can declare some classes as stable in the file we provided,
// So the compose will consider these classes as stable, we can also make external library as stable,
// without wrapping them inside stable class.

// We need to make the composable skipable so that if there is no changes in the parameter the compose function
// will not re compose, for making a compose skipable we need to make sure all the parameters are stable.
private fun Project.stabilityConfiguration() = listOf(
    "-P",
    "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=${project.rootDir.absolutePath}/compose_compiler_config.conf",
)
