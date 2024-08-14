import java.net.URI

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
//        maven(url = "s3://lk-private-maven", action = {
//            credentials(AwsCredentials::class) {
//                accessKey = System.getenv("AWS_ACCESS_KEY_ID")
//                secretKey = System.getenv("AWS_SECRET_ACCESS_KEY")
//            }
//        })
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CabelasTestApplication"
include(":app")
