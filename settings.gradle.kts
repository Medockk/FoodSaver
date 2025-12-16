rootProject.name = "FoodSaver"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://plugins.gradle.org/m2/")
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://repo1.maven.org/maven2/")
        maven("https://mirrors.aliyun.com/maven/")
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
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://repo1.maven.org/maven2/")
        maven("https://mirrors.aliyun.com/maven/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")
include(":shared")

include(":core")
include(":core:core-common")
include(":core:core-di")
include(":core:core-db")
include(":core:core-network")
include(":core:core-model")
include(":core:core-product")
include(":core:core-cart")

include(":feature-auth")
include(":feature-auth:data")
include(":feature-auth:di")
include(":feature-auth:domain")

include(":feature-home")
include(":feature-product-detail")
include(":feature-cart")