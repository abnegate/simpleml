import org.gradle.api.publish.PublishingExtension

apply(plugin = "maven-publish")

configure<PublishingExtension> {
    repositories {
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

    publications {
        create<MavenPublication>("aar") {
            groupId = project.extra["group"] as String
            artifactId = project.extra["archive"] as String
            version = project.extra["version"] as String

            artifact("$buildDir/outputs/aar/${artifactId}-release.aar")

            pom.withXml {
                val dependencies = asNode().appendNode("dependencies")
                configurations
                    .getByName("releaseCompileClasspath")
                    .resolvedConfiguration
                    .firstLevelModuleDependencies.forEach {
                        val dependency = dependencies.appendNode("dependency")
                        dependency.appendNode("groupId", it.moduleGroup)
                        dependency.appendNode("artifactId", it.moduleName)
                        dependency.appendNode("version", it.moduleVersion)
                    }
            }
        }
    }
}