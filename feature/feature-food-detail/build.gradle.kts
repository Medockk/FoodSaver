@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)

    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.koin.plugin)
}

kotlin {

    androidLibrary {
        namespace = "com.foodsaver.app.feature.food.detail"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FeatureProductDetail"
            isStatic = true
        }
    }

    jvm()

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        androidMain.dependencies {

        }

        commonMain.dependencies {
            implementation(projects.core.coreDi)
            implementation(projects.core.coreDb)
            implementation(projects.core.coreNetwork)
            implementation(projects.core.coreProduct)
            implementation(projects.core.coreCart)
            implementation(projects.core.coreCommon)
            implementation(projects.core.coreModel)
            implementation(projects.core.coreNavigation)
            implementation(projects.core.coreRestaurant)

            implementation(libs.koin.compose.viewmodel)
            implementation(libs.jetbrains.compose.navigation)

            // palette
            implementation(libs.palette.kamel)
        }
        jvmMain.dependencies {

        }
        iosMain.dependencies {

        }
        webMain.dependencies {

        }
        wasmJsMain.dependencies {

        }
        jsMain.dependencies {

        }
    }
}