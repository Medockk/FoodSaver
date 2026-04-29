import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias { libs.plugins.androidMultiplatformLibrary }
}

kotlin {

    androidLibrary {
        namespace = "com.foodsaver.app.feature.module.feature.add.product.module"
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
            implementation(projects.core.coreCommon)
            implementation(projects.core.coreModel)

            implementation(projects.core.coreNetwork)
            implementation(projects.core.coreProduct)
            implementation(projects.core.coreCategory)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.datetime)
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
