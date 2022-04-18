buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath(kotlin("gradle-plugin", version = "1.6.20"))
        classpath("com.google.gms:google-services:4.3.10")
    }
}

ext {
    set("packageGroup", "com.jakebarnby.simpleml")
    set("coreVersion", "1.1.0-beta01")
    set("objectsVersion", "1.1.0-beta01")
    set("posesVersion", "1.1.0-beta01")
    set("textVersion", "1.1.0-beta01")
}

allprojects {
    repositories {
        google()
        mavenCentral()
        //maven(properties["simpleMLRepo"] as String)
    }
}

configure(subprojects.filter { it.name.startsWith("simpleml-") }) {
    afterEvaluate {
        apply(from = "../deploy.gradle")
    }
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
