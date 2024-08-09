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
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MyPokedex"
include(":app")
include(":feature:auth:domain")
include(":feature:auth:infrastructure")
include(":feature:pokemon:domain")
include(":feature:pokemon:infrastructure")
include(":feature:tag:domain")
include(":feature:tag:infrastructure")
include(":feature:auth:ui")
include(":core:common")
include(":feature:pokemon:ui")
