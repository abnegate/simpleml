import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    kotlin("android")
}

ext {
    set("packageArchive", "simpleml-core")
    set("packageVersion", rootProject.properties["coreVersion"])
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        named("release") {
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
    aaptOptions {
        noCompress("tflite")
        noCompress("lite")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.0.0")

    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")

    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.fragment:fragment-ktx:1.2.5")

    // CameraX
    api("androidx.camera:camera-core:1.0.0-rc01")
    api("androidx.camera:camera-camera2:1.0.0-rc01")
    api("androidx.camera:camera-view:1.0.0-alpha20")
    api("androidx.camera:camera-extensions:1.0.0-alpha20")
    api("androidx.camera:camera-lifecycle:1.0.0-rc01")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:3.12.1")

    // Test
    testImplementation("junit:junit:4.13.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}