@file:Suppress("UNUSED")

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

class PrintTargetProjectKotlinVersionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val targetKotlinVersion = target.getKotlinPluginVersion()
        println("The target project is using Kotlin $targetKotlinVersion!")
    }
}
