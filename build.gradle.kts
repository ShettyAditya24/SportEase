buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.5.0")
        classpath("com.google.gms:google-services:4.3.15")
        classpath(kotlin("gradle-plugin", version = "1.8.10")) // Use consistent Kotlin version
    }
}

