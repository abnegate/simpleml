buildscript {
    val kotlin_version by extra("1.4.21")
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath(kotlin("gradle-plugin", version = "1.4.21"))
        classpath("com.google.gms:google-services:4.3.4")
    }
}

configure(subprojects.filter { it.name.startsWith("camdroid") }) {
    afterEvaluate {
        extra["group"] = "com.jakebarnby.camdroid"
        apply(from = "../deploy.gradle.kts")
    }
}

allprojects {
    repositories {
        google()
        jcenter()

        val JB_REPO: String by project
        val JB_REPO_USER: String by project
        val JB_REPO_PASSWORD: String by project

        maven(JB_REPO) {
            credentials {
                username = JB_REPO_USER
                password = JB_REPO_PASSWORD
            }
        }
    }
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
