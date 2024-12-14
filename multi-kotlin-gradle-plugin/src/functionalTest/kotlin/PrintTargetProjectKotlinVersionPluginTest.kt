import com.autonomousapps.kit.AbstractGradleProject
import com.autonomousapps.kit.GradleBuilder.build
import com.autonomousapps.kit.GradleProject
import com.autonomousapps.kit.gradle.Kotlin
import com.autonomousapps.kit.gradle.Plugin
import org.junit.Test
import kotlin.test.assertTrue

class PrintTargetProjectKotlinVersionPluginTest {
    @Test
    fun test() {
        val project = TestProject().gradleProject
        val result = build(project.rootDir, "build")
        assertTrue {
            result.output.contains("The target project is using Kotlin 2.0.0!")
        }
    }
}

class TestProject : AbstractGradleProject() {
    val gradleProject: GradleProject = build()

    private fun build(): GradleProject {
        return newGradleProjectBuilder()
            .withRootProject {
                withBuildScript {
                    kotlin = Kotlin.ofTarget(17)
                    plugins(
                        Plugin("org.jetbrains.kotlin.jvm", "2.0.0"),
                        Plugin("print-kotlin-version", "unspecified"),
                    )
                }
            }
            .write()
    }
}