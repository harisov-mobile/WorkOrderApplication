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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "WorkOrderApplication"
include(":app")
include(":common")
include(":features:login")
include(":features:synchro")
include(":features:workorders")
include(":features:workorderdetail")
include(":navigationapi")
include(":navigationimpl")
include(":core:brandbook")

 