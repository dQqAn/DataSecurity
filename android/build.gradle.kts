plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")

    id("com.google.gms.google-services")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

}

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.8.0")

    runtimeOnly("io.insert-koin:koin-core:3.4.2")// https://mvnrepository.com/artifact/io.insert-koin/koin-core
    implementation("io.insert-koin:koin-core-coroutines:3.4.1")// https://mvnrepository.com/artifact/io.insert-koin/koin-core-coroutines
    implementation("io.insert-koin:koin-androidx-compose:3.4.5")// https://mvnrepository.com/artifact/io.insert-koin/koin-androidx-compose
    implementation("io.insert-koin:koin-android:3.4.2")// https://mvnrepository.com/artifact/io.insert-koin/koin-android

    // https://mvnrepository.com/artifact/com.google.firebase/firebase-appcheck-safetynet
//    implementation("com.google.firebase:firebase-appcheck-safetynet:16.1.2")

}

android {
    namespace = "com.example.android"
    testNamespace = "com.example.testAndroid"

    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0-SNAPSHOT"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}