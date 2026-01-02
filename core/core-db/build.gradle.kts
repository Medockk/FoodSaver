import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias { libs.plugins.androidMultiplatformLibrary }
    alias(libs.plugins.sqldelight)

    alias { libs.plugins.jetbrains.kotlin.serialization }
}

kotlin {

    androidLibrary {
        namespace = "com.foodsaver.app.core.module.core.db"
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
            implementation(projects.core.coreDi)
            api(libs.sqldelight.coroutines.extensions)

            implementation(libs.kotlinx.serialization.json)
            implementation(projects.core.coreCommon)
            implementation(projects.core.coreModel)

            implementation(libs.kotlinx.datetime)
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
            verifyMigrations.set(true)
            version = 3

            generateAsync.set(true)
        }
    }
}
