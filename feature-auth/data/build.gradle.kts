@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)

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

    androidLibrary {
        namespace = "com.foodsaver.app.feature.auth.data"
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
            baseName = "FeatureAuthData"
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
            implementation(projects.core.coreDb)
            implementation(projects.core.coreNetwork)
            implementation(projects.core.coreCommon)

            implementation(projects.featureAuth.domain)
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
