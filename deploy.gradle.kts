import org.gradle.api.publish.PublishingExtension
import com.jfrog.bintray.gradle.BintrayExtension

apply(plugin = "maven-publish")

configure<BintrayExtension> {
    val BINTRAY_USER: String by project
    val BINTRAY_KEY: String by project

    user = BINTRAY_USER
    key = BINTRAY_KEY
    setPublications("aar")

    pkg = PackageConfig().apply {
        repo = "SimpleML"
        name = "gradle-project"
        userOrg = "bintray_user"
        vcsUrl = "https://github.com/abnegate/simpleml.git"
        setLicenses("GPL-3.0")

        version  = VersionConfig().apply{
            val module = (project.extra["archive"] as? String)?.split("simpleml-")?.get(1)
            val version = project.extra["version"] as String

            name = version
            released = java.util.Date().toString()
            vcsTag = "${module}/${version}"
        }
    }
}

configure<PublishingExtension> {
//    repositories {
//        val JB_REPO: String by project
//        val JB_REPO_USER: String by project
//        val JB_REPO_PASSWORD: String by project
//
//        maven(JB_REPO) {
//            credentials {
//                username = JB_REPO_USER
//                password = JB_REPO_PASSWORD
//            }
//        }
//    }

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