// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinJvm) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlinCompose) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.module.graph) apply true // Plugin applied to allow module graph generation
}

// Task to print all the module paths in the project e.g. :core:data
// Used by module graph generator script
tasks.register("printModulePaths") {
    subprojects {
        if (subprojects.size == 0) {
            println(this.path)
        }
    }
}

moduleGraphAssert {
    maxHeight = 3
    allowed = arrayOf(":features:.* -> :core:.*", ":app -> :feature.*", ":app -> :core:.*")
    restricted = arrayOf(":core.* -X> :feature.*", ":feature.* -X> :feature.*")
    assertOnAnyBuild = true
}

/**
 * ./gradlew generateModulesGraphvizText -Pmodules.graph.output.gv=all_modules || ./gradlew generateModulesGraphvizText -Pmodules.graph.of.module=:feature-one
 * ./gradlew generateModulesGraphStatistics -Pmodules.graph.output.gv=all_modules || ./gradlew generateModulesGraphStatistics -Pmodules.graph.of.module=:feature-one
 *
 * The first one is used to get a text details about the module graph, the second one is used to get a statistics about the module graph, like edge count, longest path, etc.
 * The longest path is so helpful data here.
 * */
