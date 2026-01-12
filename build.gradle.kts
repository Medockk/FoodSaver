import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false

    alias(libs.plugins.jetbrains.kotlin.serialization) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    id("com.github.gmazzo.buildconfig") version "3.1.0" apply false
    alias(libs.plugins.kotlin.android) apply false
}

subprojects {
    tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            freeCompilerArgs.addAll("-Xexpect-actual-classes")
        }
    }
}