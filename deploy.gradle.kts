apply(plugin = "maven-publish")

configure<PublishingExtension> {
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