import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") // Add this line
    id("kotlin-kapt") // Add this if using Glide
    id("kotlin-parcelize")
}

// Load local.properties
val localProperties = Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}


// Access the API_KEY from local.properties
val apiKey: String = localProperties.getProperty("TWELVEDATA_API_KEY", "")
val apiKeygemini: String = localProperties.getProperty("GEMINI_API_KEY", "")

android {
    namespace = "com.example.slapimage"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.slapimage"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        android.buildFeatures.buildConfig = true
        // Add the API_KEY to BuildConfig
        buildConfigField("String", "TWELVEDATA_API_KEY", "$apiKey")
        buildConfigField("String", "GEMINI_API_KEY", "$apiKeygemini")
        // Flag to set the build to not include all banners photo to reduce size
        buildConfigField("boolean","MINIMAL_BUILDSIZE","true")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug  {
        }
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
   // }
   // packagingOptions {
        // For .so (JNI) files
        jniLibs.pickFirsts.add("**/libonnxruntime4j_jni.so")
        jniLibs.pickFirsts.add("**/libonnxruntime.so")

        // For other files (e.g., assets, resources)
        // resources.pickFirsts.add("**/some_resource_file")
    }
}

dependencies {
    // Compose dependencies
    implementation("androidx.compose.ui:ui:1.7.8")
   // implementation("androidx.compose.material:material:1.7.8")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.8")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.json:json:20210307") // For lightweight JSON parsing

    // Material 3 dependency
    implementation("androidx.compose.material3:material3:1.3.2")
    implementation("androidx.core:core-splashscreen:1.0.1")
   // implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.9")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.9")

    // Gallery
    // RecyclerView dependency
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.chrisbanes:PhotoView:2.3.0")

    // Kotlin extensions for RecyclerView (optional but recommended)
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")

    // If you need coroutines for background operations
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")


    // Other dependencies
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("androidx.transition:transition-ktx:1.5.1")

    // Activity Result API
    implementation("androidx.activity:activity-ktx:1.10.1")
    implementation("androidx.fragment:fragment-ktx:1.8.6")  //1.6.1

    // Gemini AI
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")

    // Material Components
    //implementation("com.google.android.material:material:1.12.0")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.4.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.9")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.9")

    // Coil for image loading
    implementation("io.coil-kt:coil:2.5.0")

    // DeepSeek R1 Chatbot
    // DJL with ONNX Runtime
    implementation("ai.djl:api:0.25.0")
    implementation("ai.djl.onnxruntime:onnxruntime-engine:0.25.0")
    implementation("ai.djl:basicdataset:0.25.0")

    implementation("com.google.code.gson:gson:2.10.1")
    // For Markdown rendering
    implementation("io.noties.markwon:core:4.6.2")

    // Music Player
    implementation("com.github.lukelorusso:VerticalSeekBar:1.2.7")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.media:media:1.6.0")

    // Tetris
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.compose.ui:ui-tooling:1.7.8")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.8")
   // implementation ("androidx.compose.material3:material3:1.3.2")

    // New Calculator
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.github.fornewid:neumorphism:0.2.1")
    //implementation("com.github.medyo:android-about-page:1.3.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.28")
    // Play Core library for in-app updates
    implementation("com.google.android.play:app-update:2.1.0")

    // Play Core KTX for coroutines support (optional)
   // implementation("com.google.android.play:app-update-ktx:2.1.0")
    // Play Core Review Library (for in-app reviews)
    //implementation("com.google.android.play:review:2.0.1")

    // Optional: Kotlin extensions for coroutines support
   // implementation("com.google.android.play:review-ktx:2.0.1")
 //   implementation("com.google.android.gms:play-services-tasks:18.1.0")

    //Tic Tac Toe

    //compose material3
    implementation("androidx.compose.material:material-icons-extended:1.7.8") // must be version 1.6.4 else wouldn't work
    //material3
    implementation("androidx.compose.material3:material3-window-size-class:1.3.2") // must be version 1.2.1 else wouldn't work
    //compose navigation
    implementation("androidx.navigation:navigation-compose:2.8.9")
 //   implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.foundation:foundation:1.6.4")
    //datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0") // must be version 1.0.0 else wouldn't work
    implementation("androidx.datastore:datastore-core:1.0.0")

    // Also include coroutines if not already present
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

}
