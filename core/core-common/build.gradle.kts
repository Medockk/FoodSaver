@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)

    alias { libs.plugins.jetbrains.kotlin.serialization }
    alias { libs.plugins.composeMultiplatform }
    alias { libs.plugins.composeCompiler }
}

kotlin {

    compilerOptions {
        freeCompilerArgs.set(listOf("-Xcontext-parameters"))
    }

    androidLibrary {
        namespace = "com.foodsaver.app.core.module.core.common"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true

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
            baseName = "CoreCommon"
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
            implementation(libs.androidx.exifInterface)
        }

        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.androidx.viewModel)

            //this dependency need to compose compiler is correctly working
            implementation(libs.compose.runtime.runtime)

            implementation(libs.kotlinx.datetime)
            implementation(libs.components.components.resources)
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

compose {

    resources {
        this.publicResClass = true
        this.generateResClass = always
        this.packageOfResClass = "com.foodsaver.app.core.common.resources"
    }
}
