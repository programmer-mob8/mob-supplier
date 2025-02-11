import java.util.Properties

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {

    val path = "env.properties"
    val props = Properties()
    props.load(file(path).reader())
    val bytesafetoken = props.getProperty("bytesafetoken")

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven {
            name = "bytesafe"
            url = uri("https://mobts.bytesafe.dev/maven/mobts/")
            credentials {
                username = "bytesafe"
                password = bytesafetoken
            }
        }
    }
}


rootProject.name = "LearningKItSupplier"
include(":app")
includeBuild("../LearningKitSupplyApiService")

 