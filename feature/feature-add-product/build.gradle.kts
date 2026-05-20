import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import kotlin.collections.set

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias { libs.plugins.androidMultiplatformLibrary }

    alias { libs.plugins.jetbrains.kotlin.serialization }
    alias { libs.plugins.koin.plugin }

    // for string resources
    alias { libs.plugins.composeCompiler }
    alias { libs.plugins.composeMultiplatform }
}

kotlin {

    androidLibrary {
        namespace = "com.foodsaver.app.feature.module.feature.add.product.module"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "AddProductModule"
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
            implementation(projects.core.coreCommon)
            implementation(projects.core.coreModel)
            implementation(projects.core.coreNetwork)
            implementation(projects.core.coreProduct)
            implementation(projects.core.coreCategory)
            implementation(projects.core.coreIngredients)
            implementation(projects.core.coreAuth)
            implementation(libs.koin.compose.viewmodel)

            implementation(projects.core.coreNavigation)
            implementation(libs.jetbrains.compose.navigation)

            // for string resources
            implementation(libs.components.components.resources)
            implementation(libs.compose.runtime.runtime)
        }
        jvmMain.dependencies {

        }
        nativeMain.dependencies {

        }
        webMain.dependencies {

        }
        wasmJsMain {

        }
        jsMain.dependencies {

        }
    }
}