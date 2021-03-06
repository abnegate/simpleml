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
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

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
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")

    debugApi(project(":simpleml-core"))
    releaseApi("com.jakebarnby.simpleml:simpleml-core:${rootProject.properties["coreVersion"]}")

    // Firebase Vision
    implementation(platform("com.google.firebase:firebase-bom:26.1.1"))
    implementation("com.google.firebase:firebase-ml-vision:24.1.0")
    implementation("com.google.mlkit:object-detection:16.2.2")
    implementation("com.google.mlkit:image-labeling:17.0.1")
    // Fix BoM resolution errors
    implementation("com.google.android.gms:play-services-vision:20.1.3")
    implementation("com.google.android.gms:play-services-vision-common:19.1.3")

    // Tensorflow
    implementation("org.tensorflow:tensorflow-lite:1.10.0")
    implementation("org.tensorflow:tensorflow-lite-task-vision:0.0.0-nightly")
    implementation("org.tensorflow:tensorflow-lite-support:0.0.0-nightly")

    testImplementation("junit:junit:4.13.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}

apply(plugin = "com.google.gms.google-services")