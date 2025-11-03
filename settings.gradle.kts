pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        // AGP actualizado (evita el getJvmDefault con Kotlin >= 2.0)
        id("com.android.application") version "8.7.3"

        // Kotlin unificado
        id("org.jetbrains.kotlin.android") version "2.2.20"
        id("org.jetbrains.kotlin.plugin.compose") version "2.2.20"
        id("org.jetbrains.kotlin.plugin.serialization") version "2.2.20"

        // KSP publicado
        id("com.google.devtools.ksp") version "2.3.0"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MyGameShelf"
include(":app")
