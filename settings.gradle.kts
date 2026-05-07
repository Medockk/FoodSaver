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
        maven("https://jitpack.io")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://repo1.maven.org/maven2/")
        maven("https://mirrors.aliyun.com/maven/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")
include(":androidApp")
include(":shared")

include(":core")
include(":core:core-common")
include(":core:core-di")
include(":core:core-db")
include(":core:core-network")
include(":core:core-auth")
include(":core:core-model")
include(":core:core-product")
include(":core:core-navigation")
include(":core:core-cart")
include(":core:core-profile")
include(":core:core-paymentMethod")
include(":core:core-address")
include(":core:core-settings")
include(":core:core-category")
include(":core:core-location")
include(":core:core-fcm")
include(":core:core-restaurant")

include(":feature")

include(":feature:feature-auth")
include(":feature:feature-auth:data")
include(":feature:feature-auth:di")
include(":feature:feature-auth:domain")

include(":feature:feature-home")
include(":feature:feature-profile")
include(":feature:feature-food-detail")
include(":feature:feature-cart")
include(":feature:feature-add-product")
include(":feature:feature-restaurant")
include(":feature:feature-widget")
