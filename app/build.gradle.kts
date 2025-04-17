plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") // Add this line
    id("kotlin-kapt") // Add this if using Glide annotations
}

android {
    namespace = "com.example.slapimage"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.slapimage"
        minSdk = 21
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true // Enable Compose
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4" // Use the latest version
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Compose dependencies
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material:material:1.5.4")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("org.json:json:20210307") // For lightweight JSON parsing
    //implementation("com.google.android.material:material:1.6.0")

    // Material 3 dependency
    implementation("androidx.compose.material3:material3:1.1.2") // Use the latest version
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.google.android.material:material:1.9.0") // Use the latest version
    implementation("com.github.bumptech.glide:glide:4.16.0") // Use the latest version
    //kapt ("com.github.bumptech.glide:compiler:4.16.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5") // Use the latest version
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")

    // For Deepseek R1
    implementation("org.tensorflow:tensorflow-lite:2.8.0") // Use the latest version
    implementation("org.tensorflow:tensorflow-lite-support:0.3.1") // Optional for support libraries

    // Gallery
    // RecyclerView dependency
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    //implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.chrisbanes:PhotoView:2.3.0")

    // Kotlin extensions for RecyclerView (optional but recommended)
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")

    // If you need coroutines for background operations
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")


    // Other dependencies
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    //implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.transition:transition-ktx:1.4.1")
}