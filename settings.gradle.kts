rootProject.name = "MyKmpLearning"

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

// Android platform module
include(":androidApp")

// Umbrello module for iOS
include(":shared")

// web app module
include(":webApp")

// core modules
include(":core:core-network")
include(":core:core-domain")
include(":core:core-di")
include(":core:core-presentation")

// feature modules
include(":feature:feature-country")
include(":feature:feature-country-api")
include(":feature:feature-settings")
include(":feature:feature-settings-api")
include(":core:registry")
