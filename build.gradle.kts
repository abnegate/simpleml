buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.0-beta02")
        classpath(kotlin("gradle-plugin", version = "1.4.21"))
        classpath("com.google.gms:google-services:4.3.4")
    }
}

extra["coreVersion"] = "1.0.0-alpha04"
extra["objectsVersion"] = "1.0.0-alpha04"
extra["posesVersion"] = "1.0.0-alpha04"
extra["textVersion"] = "1.0.0-alpha04"

allprojects {
    repositories {
        google()
        jcenter()

        val JB_REPO: String by project
        val JB_REPO_USER: String by project
        val JB_REPO_PASSWORD: String by project
        val SIMPLEML_REPO: String by project

        maven(JB_REPO) {
            credentials {
                username = JB_REPO_USER
                password = JB_REPO_PASSWORD
            }
        }
        maven(SIMPLEML_REPO)
    }
}

configure(subprojects.filter { it.name.startsWith("simpleml") }) {
    afterEvaluate {
        extra["group"] = "com.jakebarnby.simpleml"
        //apply(from = "../deploy.gradle.kts")
    }
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
