@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)

    alias(libs.plugins.jetbrains.kotlin.serialization)
}

kotlin {

    androidLibrary {
        namespace = "com.foodsaver.app.shared"
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
            baseName = "Shared"
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
            // put your Multiplatform dependencies here
            api(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines)

            // for iOS target api implementation!!
            implementation(projects.core.coreNavigation)
            implementation(projects.feature.featureAuth.di)
            implementation(projects.feature.featureAuth)

            implementation(projects.core.coreDi)
            implementation(projects.core.coreAuth)
            implementation(projects.core.coreDb)
            implementation(projects.core.coreNetwork)
            implementation(projects.core.coreProduct)
            implementation(projects.core.coreCart)
            implementation(projects.core.coreProfile)
            implementation(projects.core.corePaymentMethod)
            implementation(projects.core.coreAddress)
            implementation(projects.core.coreSettings)
            implementation(projects.core.coreCategory)
            implementation(projects.core.coreLocation)
            implementation(projects.core.coreFcm)
            implementation(projects.core.coreRestaurant)

            implementation(projects.feature.featureHome)
            implementation(projects.feature.featureFoodDetail)
            implementation(projects.feature.featureCart)
            implementation(projects.feature.featureProfile)
            implementation(projects.feature.featureAddProduct)
            implementation(projects.feature.featureRestaurant)
            implementation(projects.feature.featureSearch)
            implementation(projects.feature.featurePaymentMethod)
//            implementation(projects.feature.featureWidget)
        }
        jvmMain.dependencies {

        }
        nativeMain.dependencies {

        }
        webMain.dependencies {

        }
        wasmJsMain.dependencies {

        }
        jsMain.dependencies {

        }
    }
}