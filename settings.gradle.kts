pluginManagement {
    repositories {
        google() {
            content {
                includeGroupByRegex("com\\.android.*")   // Includes Android Gradle Plugin dependencies
                includeGroupByRegex("com\\.google.*")    // Includes Google libraries
                includeGroupByRegex("androidx.*")        // Includes AndroidX libraries
            }
        }
        mavenCentral()        // Maven Central for other non-Google dependencies
        gradlePluginPortal()  // For resolving plugins from the Gradle Plugin Portal
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)  // Enforce repository declaration here only
    repositories {
        google()             // Google repository
        mavenCentral()       // Maven Central repository
    }
}

rootProject.name = "SportEase"  // Name of your project
include(":app")                // Includes the ":app" module
