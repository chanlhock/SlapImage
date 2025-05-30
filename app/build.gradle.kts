import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") // Add this line
    id("kotlin-kapt") // Add this if using Glide
    id("kotlin-parcelize")
    id("kotlinx-serialization") // Add this line
}

// Load local.properties
val localProperties = Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}


// Access the API_KEY from local.properties
val apiKey: String = localProperties.getProperty("TWELVEDATA_API_KEY", "")
val apiKeygemini: String = localProperties.getProperty("GEMINI_API_KEY", "")
val clientid: String = localProperties.getProperty("CLIENT_ID", "")
val clientsecret: String = localProperties.getProperty("CLIENT_SECRET", "")

android {
    namespace = "com.example.slapimage"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.slapimage"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        android.buildFeatures.buildConfig = true
        // Add the API_KEY to BuildConfig
        buildConfigField("String", "TWELVEDATA_API_KEY", "$apiKey")
        buildConfigField("String", "GEMINI_API_KEY", "$apiKeygemini")
        buildConfigField("String", "CLIENT_ID", "$clientid")
        buildConfigField("String", "CLIENT_SECRET", "$clientsecret")
        // Flag to set the build to not include all banners photo to reduce size
        buildConfigField("boolean","MINIMAL_BUILDSIZE","false")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        manifestPlaceholders["redirectHostName"] = "SlapImage"
        manifestPlaceholders["redirectSchemeName"] = "SlapImage"
        multiDexEnabled = true
    }

    buildTypes {
        debug  {
            //applicationIdSuffix = ".debug"
            //versionNameSuffix = "-DEBUG"
            //resValue("string","app_name","SlapImage-Debug")
            isDebuggable = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true // Enable Compose
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"  //"1.5.4" // Use the latest version
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,LICENSE,LICENSE.txt,NOTICE,NOTICE.txt}"
            excludes += "/META-INF/*.kotlin_module"
            excludes += "/META-INF/*.version"
            excludes += "/META-INF/proguard/*"
            excludes += "/META-INF/services/*"
            excludes += "/META-INF/native-image/*"
        }

        jniLibs {
            pickFirsts.addAll(listOf(
                "**/libonnxruntime4j_jni.so",
                "**/libonnxruntime.so",
                "**/libMuPDF.so"
            ))
            // Only enable if you have specific needs for uncompressed native libs
            // useLegacyPackaging = true
        }
    }
//splits {
  //      abi {
  //          reset()
  //          include("x86", "x86_64", "armeabi-v7a", "arm64-v8a")
  //          isUniversalApk = true
  //      }
 //   }

    flavorDimensions += listOf("version")

    productFlavors {
        create("fdroid") {
            minSdk = 29
            dimension = "version"
            applicationId = "com.example.slapimage"
            manifestPlaceholders["appGdriveKey"] = ""
            manifestPlaceholders["admobAppId"] = ""
            manifestPlaceholders["admobBannerId"] = ""
            manifestPlaceholders["admobFullId"] = ""
            manifestPlaceholders["appSafeMode"] = "true"
            versionNameSuffix = "-fdroid"
        }

        create("pro") {
            dimension = "version"
            applicationId = "com.example.slapimage"
            manifestPlaceholders["admobAppId"] = ""
            manifestPlaceholders["admobBannerId"] = ""
            manifestPlaceholders["admobFullId"] = ""
            manifestPlaceholders["appSafeMode"] = "false"
        }
    }

    sourceSets {
        getByName("fdroid") {
            assets.srcDirs("src/fdroid/assets", "src/fdroid/assets/")
        }
        named("main") {
            jniLibs.srcDirs("src/main/jniLibs", "src/main/Libs")
        }
    }


    applicationVariants.all {
        outputs.all {
                val flavor = productFlavors[0].name.replaceFirstChar { it.uppercase() }
                val abi = System.getenv("TARGET_ABI") ?: "universal"
                val fullName = "SlapImage_${flavor}_${versionCode}_${abi}.apk"
                (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName = fullName
        }
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

    // MP3 Tagger
    // Koin for Android
    implementation("io.insert-koin:koin-android:3.5.0")
    implementation("io.insert-koin:koin-core:3.5.0")
    // Koin for Jetpack Compose
    implementation("io.insert-koin:koin-androidx-compose:3.5.0")
    //implementation("io.insert-koin:koin-androidx-viewmodel:3.5.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("com.materialkolor:material-kolor:3.0.0-alpha04")
    implementation("io.github.dokar3:sonner:0.3.8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.3.0")
    implementation("com.github.skydoves:landscapist-coil:2.2.12")
    implementation("com.adamratzman:spotify-api-kotlin-core:4.1.0")
    implementation("com.github.nanihadesuka:LazyColumnScrollbar:2.2.0")
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
    implementation("com.github.Kyant0:taglib:1.0.0-alpha25")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
    implementation("com.soywiz.korlibs.korim:korim:3.4.0") // Must be this version

    implementation("androidx.paging:paging-runtime:3.2.1")
    implementation("androidx.paging:paging-compose:3.3.0-alpha03")
    implementation("androidx.media3:media3-exoplayer:1.2.1")
    implementation("androidx.media3:media3-ui:1.2.1")
    implementation("androidx.compose.material3:material3:1.3.2-alpha12") // or newer
    implementation("androidx.compose.material3:material3-window-size-class:1.3.2-alpha12") // if needed

    // MB Compass
    // MapLibre Native SDK
    //implementation("org.maplibre.navigation:navigation-ui-android:5.0.0-pre4")
    implementation("org.maplibre.gl:android-sdk:11.8.8")
    implementation("androidx.compose.ui:ui-viewbinding:1.6.0") // or latest version

    // XED
    api(libs.terminal.view)
    api(libs.terminal.emulator)
    api(libs.quickjs.android)
    api(libs.anrwatchdog)
    api(libs.word.wrap)
    api(libs.asynclayoutinflater)
    api(libs.gson)
    api(libs.commons.net)
    api(libs.nanohttpd)
    api(libs.browser)
    api(libs.utilcode)
    api(platform(libs.androidx.compose.bom))
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    api(project(":core:editor"))
    api(project(":core:language-textmate"))
    api(project(":core:resources"))
    api(project(":core:components"))

    // Librera
        implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
        //proImplementation(project(":pro"))
        //fdroidImplementation(project(":pro"))
        implementation(project(":pro"))
        implementation(project(":smartreflow"))

        /** AndroidX **/
        //implementation("androidx.cardview:cardview:1.0.0")
        implementation("androidx.multidex:multidex:2.0.1")
        //implementation("androidx.recyclerview:recyclerview:1.3.2")
        //implementation("androidx.work:work-runtime:2.10.0")
        implementation("androidx.legacy:legacy-support-v4:1.0.0")
        //implementation("androidx.appcompat:appcompat:1.7.0")

        /** Third-party **/
        implementation("com.github.axet:lame:1.0.9")
        implementation("org.greenrobot:eventbus:3.3.1")

        implementation("org.greenrobot:greendao:3.3.0") {
            exclude(group = "org.greenrobot.greendao.rx")
        }

        implementation("org.greenrobot:greendao-api:3.3.0")
        implementation("org.jsoup:jsoup:1.18.1")
        implementation("com.github.albfernandez:juniversalchardet:2.5.0")
        implementation("com.squareup.okhttp3:okhttp:3.12.6")
        implementation("io.github.rburgst:okhttp-digest:1.21")
        implementation("com.squareup.okio:okio-parent:1.17.6")
        implementation("com.github.joniles:rtfparserkit:1.16.0")
        implementation("org.zwobble.mammoth:mammoth:1.5.0")
        implementation("javax.xml.stream:stax-api:1.0-2")
        implementation("net.lingala.zip4j:zip4j:2.11.5")
        //implementation("com.github.bumptech.glide:glide:4.16.0")
        //annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
        implementation("commons-logging:commons-logging-api:1.1")
        implementation("androidx.work:work-runtime:2.10.0")
        implementation("com.google.guava:guava:33.3.1-android")
        implementation("com.jaredrummler:colorpicker:1.1.0")

}
