import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias { libs.plugins.androidMultiplatformLibrary }
}

kotlin {

    compilerOptions {
        freeCompilerArgs.set(listOf("-Xcontext-parameters"))
    }

    androidLibrary {
        namespace = "com.foodsaver.app.core.module.core.settings"
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
            baseName = "CoreSettings"
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

            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.androidx.datastore.preferences.core)
        }

        commonMain.dependencies {
            implementation(projects.core.coreDi)
            implementation(projects.core.coreCommon)
        }
        jvmMain.dependencies {

            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.androidx.datastore.preferences.core)
        }
        nativeMain.dependencies {

            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.androidx.datastore.preferences.core)
        }
        webMain.dependencies {

        }
        wasmJsMain {

        }
        jsMain.dependencies {

        }
    }
}
