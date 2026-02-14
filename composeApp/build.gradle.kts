@file:OptIn(ExperimentalWasmDsl::class)

import org.gradle.kotlin.dsl.buildConfig
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)

    alias(libs.plugins.jetbrains.kotlin.serialization)
    id("com.github.gmazzo.buildconfig")
}

val localProperties = Properties()
val localPropertiesFile = project.rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

buildConfig {
    packageName.set("com.foodsaver.app.composeApp")
    buildConfigField(
        type = "String",
        name = "YANDEX_MAPKIT",
        value = "\"${localProperties.getProperty("YANDEX_MAPKIT")}\""
    )
}

kotlin {

    compilerOptions {
        freeCompilerArgs.set(listOf("-Xcontext-parameters"))
    }

    androidLibrary {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        namespace = "com.foodsaver.app.composeApp"
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
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
            implementation(libs.androidx.compose.ui.tooling.preview)
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
            implementation(projects.core.coreAuth)
            implementation(projects.core.coreModel)
            implementation(projects.core.coreProduct)
            implementation(projects.core.coreCart)
            implementation(projects.core.coreProfile)
            implementation(projects.core.coreNavigation)
            implementation(projects.core.corePaymentMethod)
            implementation(projects.core.coreAddress)
            implementation(projects.core.coreSettings)

            implementation(projects.featureAuth)
            implementation(projects.featureAuth.di)

            implementation(projects.featureHome)
            implementation(projects.featureProductDetail)
            implementation(projects.featureCart)
            implementation(projects.featureProfile)

            implementation(libs.image.picker)
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

compose.desktop {
    application {
        mainClass = "com.foodsaver.app.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "com.foodsaver.app"
            packageVersion = "1.0.0"
        }
    }
}