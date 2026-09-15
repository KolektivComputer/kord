import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SourcesJar

plugins {
    com.vanniktech.maven.publish
}

// signing is only configured when signing credentials are available, so that publishToMavenLocal works locally
val hasSigningCredentials = listOf(
    "signingInMemoryKey",
    "signing.key",
    "signing.secretKeyRingFile",
).any { providers.gradleProperty(it).isPresent }

mavenPublishing {
    coordinates(Library.group, "kord-${project.name}", libraryVersion.get())

    publishToMavenCentral(automaticRelease = true)
    if (hasSigningCredentials) signAllPublications()

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
        val githubActor = providers.environmentVariable("GITHUB_ACTOR")
        val githubToken = providers.environmentVariable("GITHUB_TOKEN")
        if (githubActor.isPresent && githubToken.isPresent) {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/KolektivComputer/kord")

                credentials {
                    username = githubActor.get()
                    password = githubToken.get()
                }
            }
        }

        val yuriUsername = providers.gradleProperty("kord.publishing.yuriCapitalRepoUsername")
            .orElse(providers.environmentVariable("YURI_CAPITAL_REPO_USERNAME"))
        val yuriPassword = providers.gradleProperty("kord.publishing.yuriCapitalRepoPassword")
            .orElse(providers.environmentVariable("YURI_CAPITAL_REPO_PASSWORD"))
        if (yuriUsername.isPresent && yuriPassword.isPresent) {
            maven {
                name = "yuriReleases"
                url = uri("https://repo.yuri.capital/repository/maven-releases/")

                credentials {
                    username = yuriUsername.get()
                    password = yuriPassword.get()
                }
            }
        }
    }
}
