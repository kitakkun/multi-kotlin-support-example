plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.gradleTestKitSupportPlugin)
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        register("print-kotlin-version") {
            id = "print-kotlin-version"
            implementationClass = "PrintTargetProjectKotlinVersionPlugin"
        }
    }
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    functionalTestImplementation(libs.kotlin.test.junit)
    functionalTestImplementation(libs.gradle.testkit.support)
    functionalTestImplementation(libs.gradle.testkit.truth)
}
