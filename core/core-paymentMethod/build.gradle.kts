import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias { libs.plugins.androidMultiplatformLibrary }
    alias { libs.plugins.jetbrains.kotlin.serialization }
}

kotlin {

    androidLibrary {
        namespace = "com.foodsaver.app.core.module.core.paymentMethod"
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
            baseName = "CorePaymentMethod"
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
            implementation(projects.core.coreNetwork)
            implementation(projects.core.coreDb)
            implementation(projects.core.coreDi)
            implementation(projects.core.coreAuth)
            implementation(projects.core.coreCommon)
            implementation(projects.core.coreModel)
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
