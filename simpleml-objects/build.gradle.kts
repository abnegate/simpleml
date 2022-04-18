import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    kotlin("android")
}

ext {
    set("packageArchive", "simpleml-objects")
    set("packageVersion", rootProject.properties["objectsVersion"])
}

android {
    compileSdk = 32
    defaultConfig {
        minSdk = 21
        targetSdk = 32
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")

    debugApi(project(":simpleml-core"))
    releaseApi("com.jakebarnby.simpleml:simpleml-core:${rootProject.properties["coreVersion"]}")

    // ML Kit
    implementation("com.google.mlkit:object-detection:17.0.0")
    implementation("com.google.mlkit:image-labeling:17.0.7")

    // Tensorflow
    implementation("org.tensorflow:tensorflow-lite:2.7.0")
    implementation("org.tensorflow:tensorflow-lite-task-vision:0.3.0")
    implementation("org.tensorflow:tensorflow-lite-gpu-delegate-plugin:0.3.0")
    implementation("org.tensorflow:tensorflow-lite-support-api:0.3.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}