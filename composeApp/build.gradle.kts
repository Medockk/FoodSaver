@file:OptIn(ExperimentalWasmDsl::class, ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
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

val isMac = System.getProperty("os.name").contains("Mac", ignoreCase = true)

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
        namespace = "com.foodsaver.app.composeApp.androidMain"
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    sourceSets.getByName("androidMain") {
        resources.srcDirs("src/androidMain/res")
    }

    if (isMac) {

        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = "ComposeApp"
                isStatic = true

                freeCompilerArgs += listOf("-Xbinary=bundleId=com.foodsaver.app.ComposeApp")

                export(projects.core.coreDi)
                export(projects.shared)
            }
        }
    }

    jvm()

    js {
        browser()
        binaries.executable()
    }

//    wasmJs {
//        browser()
//        binaries.executable()
//    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.splash)
            implementation(libs.compose.ui.tooling)
            implementation(libs.yandex.mapkit)

            implementation(libs.kotlin.testJunit)
            implementation(libs.androidx.testExt.junit)
            implementation(libs.androidx.espresso.core)
            implementation(libs.androidx.compose.ui.test.junit4)

            implementation(libs.compose.ui.ui)
        }
        commonMain.dependencies {
            implementation(project.dependencies.platform(libs.androidx.compose.bom))
            implementation(libs.compose.runtime.runtime)
            implementation(libs.compose.foundation.foundation)
            implementation(libs.compose.ui.ui)
            implementation(libs.compose.animation.animation)
            implementation(libs.components.components.resources)
            implementation(libs.compose.ui.tooling.preview)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.kotlinx.serialization.json)

            implementation(libs.jetbrains.compose.navigation)
            implementation(libs.material3)

            implementation(libs.koin.compose.navigation)
            implementation(libs.koin.core)

            implementation(libs.kotlinx.coroutines)
            implementation(libs.bundles.coil)

            // for iOS and XCode
            api(projects.shared)
            api(projects.core.coreDi)

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
            implementation(projects.core.coreCategory)
            implementation(projects.core.coreLocation)
            implementation(projects.core.coreFcm)
            implementation(projects.core.coreRestaurant)

            implementation(projects.feature.featureAuth)
            implementation(projects.feature.featureAuth.di)

            implementation(projects.feature.featureHome)
            implementation(projects.feature.featureFoodDetail)
            implementation(projects.feature.featureCart)
            implementation(projects.feature.featureProfile)
            implementation(projects.feature.featureAddProduct)
            implementation(projects.feature.featureRestaurant)
            implementation(projects.feature.featureSearch)
            implementation(projects.feature.featurePaymentMethod)

            implementation(libs.image.picker)

            implementation(libs.palette.kamel)
            implementation(libs.palette.kamel.default)

            implementation(libs.kmpalette.kmpalette.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ui.test)
            implementation(compose.uiTest)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.compose.ui.tooling)
        }
        iosMain.dependencies {

        }

        webMain.dependencies {
            implementation(libs.kotlinx.browser)
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = always
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