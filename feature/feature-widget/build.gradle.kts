import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias { libs.plugins.androidMultiplatformLibrary }
    alias { libs.plugins.composeCompiler }

    alias { libs.plugins.jetbrains.kotlin.serialization }
}

kotlin {

    androidLibrary {
        namespace = "com.foodsaver.app.feature.widget"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
//        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true

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
            baseName = "FeatureWidget"
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
            implementation(libs.androidx.glance.appwidget)
            implementation(libs.androidx.glance.material3)
            implementation(libs.androidx.work.runtime)
            implementation(libs.androidx.work.ktx)

            implementation(libs.compose.runtime.runtime)
            implementation(libs.androidx.work.koin)
        }

        commonMain.dependencies {
            implementation(projects.core.coreDi)
            implementation(projects.core.coreDb)
            implementation(projects.core.coreCommon)
            implementation(projects.core.coreModel)
            implementation(projects.core.coreProduct)

            implementation(libs.kotlinx.serialization.json)
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
