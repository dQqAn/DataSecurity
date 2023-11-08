plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
//    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("kotlin-parcelize")
}

group = "com.example"
version = "1.0-SNAPSHOT"

kotlin {
    androidTarget()
    jvm("desktop") {
        jvmToolchain(17)
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.ui)
                api(compose.animation)
                api(compose.material3) // api(compose.material)
                api(compose.materialIconsExtended)

                runtimeOnly("io.insert-koin:koin-core:3.4.2")// https://mvnrepository.com/artifact/io.insert-koin/koin-core
                implementation("io.insert-koin:koin-core-coroutines:3.4.1")// https://mvnrepository.com/artifact/io.insert-koin/koin-core-coroutines

                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class) implementation(compose.components.resources)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")//https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx3:1.7.3") // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-rx3
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.12.0")
                implementation("androidx.activity:activity-compose:1.8.0")

                implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")// https://mvnrepository.com/artifact/androidx.lifecycle/lifecycle-viewmodel-compose

                implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha")

                implementation(platform("com.google.firebase:firebase-bom:32.5.0")) // https://mvnrepository.com/artifact/com.google.firebase/firebase-bom
                implementation("com.google.firebase:firebase-auth-ktx")// When using the BoM, you don't specify versions in Firebase library dependencies
                implementation("com.google.firebase:firebase-firestore-ktx")
                implementation("com.google.firebase:firebase-database-ktx")
                implementation("com.google.firebase:firebase-storage")

                // https://mvnrepository.com/artifact/androidx.security/security-crypto
                implementation("androidx.security:security-crypto:1.1.0-alpha06")

            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
            }
        }
    }
}

android {
    namespace = "com.example.common.src"
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}