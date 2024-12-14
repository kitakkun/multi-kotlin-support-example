plugins {
    alias(libs.plugins.kotlinJvm)
}

val kotlinVersion = libs.versions.kotlin.get().parseToKotlinVersion()
val versionSpecificSrcDirRegex = "^(v|pre)(_\\d){1,3}\\d?$".toRegex()

kotlin {
    sourceSets.forEach { sourceSet ->
        val srcDirs = sourceSet.kotlin.srcDirs
        val sourceSetRootPath = srcDirs.first().toPath().parent

        val newSrcDirs = srcDirs.toMutableSet()
        newSrcDirs += sourceSetRootPath.resolve("core").toFile()

        val versionSpecificSrcDirs = sourceSetRootPath.toFile().listFiles().orEmpty().filter {
            it.name.matches(versionSpecificSrcDirRegex) && it.exists()
        }

        val versionSpecificSrcDirsMap = versionSpecificSrcDirs.associateBy {
            val kotlinVersion = it.name.substringAfter("_") // v_2_0_0 -> 2_0_0, pre_2_0_0 -> 2_0_0
                .replace("_", ".") // 2_0_0 -> 2.0.0
                .parseToKotlinVersion()
            when {
                it.name.startsWith("v_") -> RangedKotlinVersion.Direct(kotlinVersion)
                it.name.startsWith("pre_") -> RangedKotlinVersion.Pre(kotlinVersion)
                else -> error("Unexpected source directory name: ${it.name}.")
            }
        }.toList()

        val directVersionSpecificSrcDir = versionSpecificSrcDirsMap.find { (version, _) ->
            version is RangedKotlinVersion.Direct && version.matches(kotlinVersion)
        }?.second

        val preVersionSpecificSrcDir = versionSpecificSrcDirsMap.find { (version, _) ->
            version is RangedKotlinVersion.Pre && version.matches(kotlinVersion)
        }?.second

        val targetVersionSpecificSrcDir = when {
            directVersionSpecificSrcDir != null -> directVersionSpecificSrcDir
            preVersionSpecificSrcDir != null -> preVersionSpecificSrcDir
            else -> sourceSetRootPath.resolve("latest").toFile()
        }

        newSrcDirs += targetVersionSpecificSrcDir
        sourceSet.kotlin.setSrcDirs(newSrcDirs)
    }
}

data class KotlinVersion(
    val majorVersion: Int,
    val minorVersion: Int,
    val patchVersion: Int,
) {
    override fun toString(): String {
        return "$majorVersion.$minorVersion.$patchVersion"
    }

    operator fun compareTo(other: KotlinVersion): Int {
        return when {
            this.majorVersion != other.majorVersion -> this.majorVersion - other.majorVersion
            this.minorVersion != other.minorVersion -> this.minorVersion - other.minorVersion
            else -> this.patchVersion - other.patchVersion
        }
    }
}

fun String.parseToKotlinVersion(): KotlinVersion {
    val (major, minor, patch) = substringBefore("-").split(".").map { it.toInt() }
    return KotlinVersion(major, minor, patch)
}

sealed interface RangedKotlinVersion {
    fun matches(kotlinVersion: KotlinVersion): Boolean

    data class Direct(val kotlinVersion: KotlinVersion) : RangedKotlinVersion {
        override fun matches(kotlinVersion: KotlinVersion) = kotlinVersion == this.kotlinVersion
    }

    data class Pre(val kotlinVersion: KotlinVersion) : RangedKotlinVersion {
        override fun matches(kotlinVersion: KotlinVersion) = kotlinVersion <= this.kotlinVersion
    }
}
