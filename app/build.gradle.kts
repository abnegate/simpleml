plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "com.jakebarnby.samplesimpleml"
        minSdk = 21
        targetSdk = 32
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
    buildFeatures {
        dataBinding = true
    }
    packagingOptions {
        resources {
            excludes += setOf("META-INF/proguard/androidx-annotations.pro")
        }
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

    implementation(kotlin("stdlib", org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION))
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    debugImplementation(project(":simpleml-objects"))
    debugImplementation(project(":simpleml-poses"))
    debugImplementation(project(":simpleml-text"))
//    implementation("com.jakebarnby.simpleml:simpleml-objects:${rootProject.properties["objectsVersion"]}")
//    implementation("com.jakebarnby.simpleml:simpleml-poses:${rootProject.properties["posesVersion"]}")
//    implementation("com.jakebarnby.simpleml:simpleml-text:${rootProject.properties["textVersion"]}")
}
