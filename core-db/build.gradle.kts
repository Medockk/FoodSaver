@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqldelight)
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
            baseName = "CoreDB"
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
            implementation(libs.sqldelight.android.driver)
            implementation(libs.sqldelight.runtime)
        }

        commonMain.dependencies {
            implementation(projects.coreDi)
            implementation(libs.sqldelight.coroutines.extensions)
        }
        jvmMain.dependencies {
            implementation(libs.sqldelight.jvm.driver)
            implementation(libs.sqldelight.runtime)
        }
        nativeMain.dependencies {
            implementation(libs.sqldelight.native.driver)
            implementation(libs.sqldelight.runtime)
        }
        webMain.dependencies {
            implementation(libs.sqldelight.web.driver)
        }
        wasmJsMain {
            //dependsOn(jsMain.get())
        }
        jsMain.dependencies {
            implementation(libs.sqldelight.web.driver)

            implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.1.0"))
            implementation("app.cash.sqldelight:runtime-js:2.0.2")
            implementation(npm("sql.js", "1.8.0"))

            implementation(npm("path-browserify", "1.0.1"))
            implementation(npm("crypto-browserify", "3.12.0"))
            implementation(npm("stream-browserify", "3.0.0"))
            implementation(npm("buffer", "6.0.3"))
            implementation(npm("vm-browserify", "1.1.2"))
        }
    }
}

sqldelight {
    databases {
        create("MainAppDatabase") {
            packageName.set("com.databases.cache")
            //verifyMigrations.set(true)

            generateAsync.set(true)
        }
    }
}

android {
    namespace = "com.foodsaver.app.core.db"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
