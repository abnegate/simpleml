buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-alpha04")
        classpath(kotlin("gradle-plugin", version = "1.4.21"))
        classpath("com.google.gms:google-services:4.3.4")
    }
}

extra["coreVersion"] = "1.0.0-beta02"
extra["objectsVersion"] = "1.0.0-beta01"
extra["posesVersion"] = "1.0.0-beta01"
extra["textVersion"] = "1.0.0-beta01"

allprojects {
    repositories {
        google()
        jcenter()

        val SIMPLEML_REPO: String by project
        maven(SIMPLEML_REPO)
    }
}

configure(subprojects.filter { it.name.startsWith("simpleml") }) {
    afterEvaluate {
        extra["group"] = "com.jakebarnby.simpleml"
        apply(from = "../deploy.gradle.kts")
    }
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
