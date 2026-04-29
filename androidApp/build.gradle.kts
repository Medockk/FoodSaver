plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.foodsaver.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    version = "1.0.0-debug"

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

    configurations.all {
        resolutionStrategy {
            force("androidx.datastore:datastore-preferences:1.2.0")
            force("androidx.datastore:datastore-preferences-core:1.2.0")
            force("androidx.datastore:datastore-core:1.2.0")
        }
    }
}

dependencies {

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.jetbrains.compose.navigation)

    implementation(projects.composeApp)
    implementation(projects.shared)
    implementation(projects.core.coreFcm)
    implementation(projects.core.coreDi)
    implementation(projects.core.coreNavigation)
    implementation(projects.feature.featureWidget)
    api(projects.core.coreCommon)

    implementation(libs.androidx.splash)
    implementation(libs.components.components.resources)
    implementation(libs.compose.runtime.runtime)
    implementation(libs.androidx.work.koin)
}