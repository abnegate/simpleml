plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.jakebarnby.sampleclassifier"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android.txt"),
                    "proguard-rules.pro"
                )
            )
        }
    }
    packagingOptions {
        exclude("META-INF/proguard/androidx-annotations.pro")
    }
    aaptOptions {
        noCompress("tflite")
        noCompress("lite")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(kotlin("stdlib", org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION))
    implementation(kotlin("coroutines-core", "1.3.7"))
    implementation(kotlin("coroutines-android", "1.3.3"))
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")

    testImplementation("junit:junit:4.13.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

    implementation("com.jakebarnby.camdroid:camdroid-core:1.0.0-alpha01")
    implementation("com.jakebarnby.camdroid:camdroid-objects:1.0.0-alpha01")
    implementation("com.jakebarnby.camdroid:camdroid-poses:1.0.0-alpha01")
    implementation("com.jakebarnby.camdroid:camdroid-text:1.0.0-alpha01")
}
