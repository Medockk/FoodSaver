@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias { libs.plugins.jetbrains.kotlin.serialization }
    id("com.github.gmazzo.buildconfig")
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
        name = "GOOGLE_CLIENT_ID_JS",
        value = "\"${localProperties.getProperty("GOOGLE_CLIENT_ID_JS")}\""
    )
    buildConfigField(
        type = "String",
        name = "GOOGLE_CLIENT_SECRET_JVM",
        value = "\"${localProperties.getProperty("GOOGLE_CLIENT_SECRET_JVM")}\""
    )
}

kotlin {
    androidTarget() {
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
            baseName = "FeatureAuth"
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
            implementation(libs.androidx.credentials)
            implementation(libs.googleid)
        }

        commonMain.dependencies {
            implementation(projects.coreDi)
            implementation(projects.coreDb)
            implementation(projects.coreNetwork)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.koin.compose.viewmodel)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.netty)
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

android {
    namespace = "com.foodsaver.app.feature.auth"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
