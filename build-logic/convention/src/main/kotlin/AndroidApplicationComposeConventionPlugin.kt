import com.android.build.api.dsl.ApplicationExtension
import com.basim.block.BuildUtil.ANDROID_APPLICATION_PLUGIN
import com.basim.block.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(ANDROID_APPLICATION_PLUGIN)
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
        }
    }
}