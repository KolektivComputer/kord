import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SourcesJar

plugins {
    com.vanniktech.maven.publish
}

mavenPublishing {
    coordinates(Library.group, "kord-${project.name}", libraryVersion.get())

    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    pom {
        name = Library.name
        description = Library.description
        url = Library.projectUrl

        organization {
            name = "Kord"
            url = "https://github.com/kordlib"
        }

        developers {
            developer {
                name = "The Kord Team"
            }
        }

        issueManagement {
            system = "GitHub"
            url = "https://github.com/kordlib/kord/issues"
        }

        licenses {
            license {
                name = "MIT"
                url = "https://opensource.org/licenses/MIT"
            }
        }

        scm {
            connection = "scm:git:ssh://github.com/kordlib/kord.git"
            developerConnection = "scm:git:ssh://git@github.com:kordlib/kord.git"
            url = Library.projectUrl
        }
    }

    if (plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
        configure(
            KotlinMultiplatform(
                JavadocJar.Dokka("dokkaGeneratePublicationHtml"),
                sourcesJar = SourcesJar.Sources()
            )
        )
    } else if (plugins.hasPlugin("org.jetbrains.kotlin.jvm")) {
        configure(KotlinJvm(JavadocJar.Dokka("dokkaGeneratePublicationHtml"), sourcesJar = SourcesJar.Sources()))
    }
}

publishing {
    repositories {

        val ghActor = providers.environmentVariable("GITHUB_ACTOR")
        val ghToken = providers.environmentVariable("GITHUB_TOKEN")
        if (ghActor.isPresent && ghToken.isPresent) {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/KolektivComputer/kord")
                credentials {
                    username = ghActor.get()
                    password = ghToken.get()
                }
            }
        }

        // Legacy upstream GH Packages (optional; prefer KolektivComputer above)
        maven {
            url = uri("https://maven.pkg.github.com/kordlib/kord")

            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }

        val yuriUser = providers.environmentVariable("YURI_CAPITAL_REPO_USERNAME")
        val yuriPass = providers.environmentVariable("YURI_CAPITAL_REPO_PASSWORD")
        if (yuriUser.isPresent && yuriPass.isPresent) {
            val snapshot = libraryVersion.get().endsWith("-SNAPSHOT", ignoreCase = true)
            maven {
                name = if (snapshot) "yuriSnapshots" else "yuriReleases"
                url = uri(
                    if (snapshot) "https://repo.yuri.capital/repository/maven-snapshots/"
                    else "https://repo.yuri.capital/repository/maven-releases/"
                )
                credentials {
                    username = yuriUser.get()
                    password = yuriPass.get()
                }
            }
        }
    }
}
