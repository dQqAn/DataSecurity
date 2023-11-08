pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("multiplatform").version(extra["kotlin.version"] as String)
        kotlin("android").version(extra["kotlin.version"] as String)
        id("com.android.application").version(extra["agp.version"] as String)
        id("com.android.library").version(extra["agp.version"] as String)
        id("org.jetbrains.compose").version(extra["compose.version"] as String)
        kotlin("plugin.serialization").version(extra["kotlin.version"] as String)

        id("com.google.gms.google-services").version("4.3.15")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "DataSecurity"

include(":android", ":desktop", ":common")
