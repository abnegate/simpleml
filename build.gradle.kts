buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-alpha04")
        classpath(kotlin("gradle-plugin", version = "1.4.21"))
        classpath("com.google.gms:google-services:4.3.4")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
    }
}

ext {
    set("packageGroup", "com.jakebarnby.simpleml")
    set("coreVersion", "1.0.0-beta02")
    set("objectsVersion", "1.0.0-beta02")
    set("posesVersion", "1.0.0-beta02")
    set("textVersion", "1.0.0-beta02")
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(properties["simpleMLRepo"] as String)
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
