plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.dagger.hilt.plugin)
    id("kotlin-kapt")
}

android {
    namespace = "coder.behzod"
    compileSdk = 34

    defaultConfig {
        applicationId = "coder.behzod"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.workManager)
    implementation(libs.androidx.hilt.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(project(":data"))
    implementation(project(":domain"))

//  Dagger Hilt
    implementation(libs.daggerHilt.navigation.compose)
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)
    kapt(libs.dagger.hilt.android.compiler)

//  Room
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.coroutines)
    kapt(libs.room.kapt.compiler)

//  Lottie Animations
    implementation(libs.lottie.animations)

//  Navigation
    implementation(libs.androidx.navigation)

    implementation(libs.liveData)

//  Coroutines
    implementation(libs.kotlinx.coroutines)

//    SSJetPackComposeProgressButton
    implementation(libs.ss.progressButton)

//    SpeedDialFloatingActionButton
    implementation(libs.speedDialFabCompose)

//    RevealSwipe
    implementation(libs.revealSwipe)
}