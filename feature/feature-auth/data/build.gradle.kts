@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)

    alias { libs.plugins.jetbrains.kotlin.serialization }
    id("com.github.gmazzo.buildconfig")
    id("org.jetbrains.kotlin.native.cocoapods") apply false // отключаем чтобы на винде оно нне ломало сборку
}

val localProperties = Properties()
val localPropertiesFile = project.rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

buildConfig {
    packageName.set("com.foodsaver.app.feature.auth.config")
    buildConfigField(
        type = "String",
        name = "GOOGLE_CLIENT_ID_ANDROID",
        value = "\"${localProperties.getProperty("GOOGLE_CLIENT_ID_ANDROID")}\""
    )
    buildConfigField(
        type = "String",
        name = "GOOGLE_CLIENT_ID_JVM",
        value = "\"${localProperties.getProperty("GOOGLE_CLIENT_ID_JVM")}\""
    )
    buildConfigField(
        type = "String",
        name = "GOOGLE_CLIENT_ID_WEB",
        value = "\"${localProperties.getProperty("GOOGLE_CLIENT_ID_WEB")}\""
    )
    buildConfigField(
        type = "String",
        name = "GOOGLE_CLIENT_SECRET_JVM",
        value = "\"${localProperties.getProperty("GOOGLE_CLIENT_SECRET_JVM")}\""
    )
}

kotlin {

    val isMac = System.getProperty("os.name").startsWith("Mac", ignoreCase = true)

    if (isMac) {
        // применяем плагин на Маке
        plugins.apply("org.jetbrains.kotlin.native.cocoapods")

        // Объявляем iOS таргеты
        iosX64()
        iosArm64()
        iosSimulatorArm64()

        // Конфигурируем cocoapods динамически, чтобы обойти ошибку компиляции .kts
        configure<org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension> {
            summary = "Authentication data module for FoodSaver"
            homepage = "https://github.com/Medockk/FoodSaver"
            version = "1.0"
            ios.deploymentTarget = "14.1" // версия таргета для ios УЗКАТЬ ИЗ XCODE КАКОЙ МИНИМАЛЬНЫЙ ТАРГЕТ]!!!!

//            url("https://github.com/CocoaPods/Specs.git")

            framework {
                baseName = "FeatureAuthData"
                isStatic = true
            }

            pod("GoogleSignIn") {
                version = "7.1.0"
            }
        }
    }

    androidLibrary {
        namespace = "com.foodsaver.app.feature.auth.data"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = "FeatureAuthData"
//            isStatic = true
//        }
//    }

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
            implementation(libs.androidx.credentials)
            implementation(libs.androidx.credentials.lite)
            implementation(libs.googleid)
        }

        commonMain.dependencies {
            implementation(projects.core.coreAuth)
            implementation(projects.core.coreDb)
            implementation(projects.core.coreNetwork)
            implementation(projects.core.coreCommon)

            implementation(projects.feature.featureAuth.domain)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.netty)
        }
    }
}
