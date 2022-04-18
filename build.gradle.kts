buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath(kotlin("gradle-plugin", version = "1.6.20"))
    }
}

ext {
    set("packageGroup", "com.jakebarnby.simpleml")
    set("coreVersion", "1.1.0-beta02")
    set("objectsVersion", "1.1.0-beta02")
    set("posesVersion", "1.1.0-beta02")
    set("textVersion", "1.1.0-beta02")
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.jakebarnby.com") {
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
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
