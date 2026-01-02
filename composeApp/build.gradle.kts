@file:OptIn(ExperimentalWasmDsl::class, ExperimentalKotlinGradlePluginApi::class)

import com.android.build.api.dsl.androidLibrary
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)

    alias(libs.plugins.jetbrains.kotlin.serialization)
}

kotlin {

    compilerOptions {
        freeCompilerArgs.set(listOf("-Xcontext-parameters"))
    }

    androidLibrary {

        namespace = "com.foodsaver.app"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        version = 1

//        defaultConfig {
//            applicationId = "com.foodsaver.app"
//            targetSdk = libs.versions.android.targetSdk.get().toInt()
//            versionCode = 1
//            versionName = "1.0"
//        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
//        buildTypes {
//            getByName("release") {
//                isMinifyEnabled = false
//            }
//        }
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)

//            sourceCompatibility = JavaVersion.VERSION_11
//            targetCompatibility = JavaVersion.VERSION_11
        }
    }

    jvm()

    js {
        browser()
        binaries.executable()
    }

    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.splash)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.animation)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.kotlinx.serialization.json)

            implementation(libs.jetbrains.compose.navigation)
            implementation(libs.material3)

            implementation(libs.koin.compose.navigation)
            implementation(libs.koin.core)

            implementation(libs.kotlinx.coroutines)
            implementation(libs.bundles.coil)

            implementation(projects.shared)

            implementation(projects.core.coreDi)
            implementation(projects.core.coreDb)
            implementation(projects.core.coreNetwork)
            implementation(projects.core.coreCommon)
            implementation(projects.core.coreModel)
            implementation(projects.core.coreProduct)
            implementation(projects.core.coreCart)
            implementation(projects.core.coreProfile)

            implementation(projects.featureAuth)
            implementation(projects.featureAuth.di)

            implementation(projects.featureHome)
            implementation(projects.featureProductDetail)
            implementation(projects.featureCart)
            implementation(projects.featureProfile)
        }
//        commonTest.dependencies {
//            implementation(libs.kotlin.test)
//        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }

        webMain.dependencies {
            implementation(libs.kotlinx.browser)
        }
    }
}

//dependencies {
//    debugImplementation(compose.uiTooling)
//}

compose {
    desktop {
        application {
            mainClass = "com.foodsaver.app.MainKt"

            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
                packageName = "com.foodsaver.app"
                packageVersion = "1.0.0"
            }
        }
    }

    resources {
        packageOfResClass = "foodsaver.composeapp.generated.resources"
        generateResClass = ResourcesExtension.ResourceClassGeneration.Always
    }
}