rootProject.name = "multi-kotlin-support-example"

pluginManagement {
    repositories {
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files(rootDir.toPath().resolve("versions-root/libs.versions.toml")))

            System.getenv("KOTLIN_VERSION")?.let {
                version("kotlin", it)
            }
        }
    }
}

include(":multi-kotlin-module")
include(":multi-kotlin-gradle-plugin")
