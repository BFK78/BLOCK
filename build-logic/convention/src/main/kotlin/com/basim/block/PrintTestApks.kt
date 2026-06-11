package com.basim.block

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.BuiltArtifactsLoader
import com.android.build.api.variant.HasAndroidTest
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import java.io.File

/**
This code snippet defines a custom Gradle task for printing the location of the Android test APK for a specific variant.
 */

internal fun Project.configurePrintApksTask(extension: AndroidComponentsExtension<*, *, *>) {

    extension.onVariants { variant ->

        if (variant is HasAndroidTest) {
            /**
             * Artifact refers to a specific file or directory produced during the build process.
             * These artifacts can represent various outputs, including compiled code, resources, assets, and more.
             */
            val loader = variant.artifacts.getBuiltArtifactsLoader()
            val artifact = variant.androidTest?.artifacts?.get(SingleArtifact.APK)
            val javaSources = variant.androidTest?.sources?.java?.all
            val kotlinSources = variant.androidTest?.sources?.kotlin?.all

            val testSources = if (javaSources != null && kotlinSources != null) {
                javaSources.zip(kotlinSources) { javaDirs, kotlinDirs ->
                    javaDirs + kotlinDirs
                }
            } else javaSources ?: kotlinSources

            if (artifact != null && testSources != null) {
                tasks.register(
                    "${variant.name}PrintTestApk",
                    PrintApkLocationTask::class.java
                ) {
                    apkFolder.set(artifact)
                    builtArtifactsLoader.set(loader)
                    variantName.set(variant.name)
                    sources.set(testSources)
                }
            }
        }
    }
}

// Gradle actually cache a task's output, so we don't need that here because we are printing output.
@DisableCachingByDefault(because = "Prints output")
internal abstract class PrintApkLocationTask : DefaultTask() {

    /**
     * PathSensitive =>
     * Annotates a task file property, specifying which part of the file paths should be considered during up-to-date checks.
     */
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:InputDirectory // Marks a property as specifying an input directory for a task.
    abstract val apkFolder: DirectoryProperty

    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:InputFiles
    abstract val sources: ListProperty<Directory>

    /**
     * Attached to a task property to indicate that the property is not to be taken into account for up-to-date checking.
     */
    @get:Internal
    abstract val builtArtifactsLoader: Property<BuiltArtifactsLoader>

    /**
     * Attached to a task property to indicate that the property specifies some input value for the task.
     */
    @get:Input
    abstract val variantName: Property<String>

    @TaskAction
    fun taskAction() {
        val hasFiles = sources.orNull?.any { directory ->
            directory.asFileTree.files.any {
                it.isFile && "build${File.separator}generated" !in it.parentFile.path
            }
        } ?: throw RuntimeException("Cannot check androidTest sources")

        // Don't print APK location if there are no androidTest source files
        if (!hasFiles) return

        val builtArtifacts = builtArtifactsLoader.get().load(apkFolder.get())
            ?: throw RuntimeException("Cannot load APKs")
        if (builtArtifacts.elements.size != 1)
            throw RuntimeException("Expected one APK !")
        val apk = File(builtArtifacts.elements.single().outputFile).toPath()
        println(apk)
    }
}

/**
 * GradleUpToDateTask Execution =>
 * Gradle's up-to-date check is a mechanism that determines whether a task needs to be executed or not.
 * It does this by comparing the task's inputs and outputs.
 * If the inputs and outputs have not changed since the last time the task was executed,
 * then Gradle will consider the task to be up-to-date and will not execute it.
 * The up-to-date check is a very important part of Gradle's incremental build system.
 * Incremental builds allow Gradle to only execute the tasks that need to be executed,
 * which can save a lot of time.
 */
