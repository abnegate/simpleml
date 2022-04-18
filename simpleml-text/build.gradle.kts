import org.jetbrains.kotlin.config.KotlinCompilerVersion


plugins {
    id("com.android.library")
    kotlin("android")
}

ext {
    set("packageArchive", "simpleml-text")
    set("packageVersion", rootProject.properties["textVersion"])
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
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
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
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:18.0.0")
    implementation("com.google.mlkit:language-id:17.0.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

apply(plugin = "com.google.gms.google-services")