plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // Google services plugin for Firebase
}

android {
    namespace = "com.example.sportease"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sportease"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true // Enable view binding for easier UI manipulation
    }
}

dependencies {
    // AndroidX and Material dependencies
    implementation("androidx.appcompat:appcompat:1.6.1")          // Support for backward compatibility
    implementation("com.google.android.material:material:1.9.0")   // Material design components
    implementation("androidx.activity:activity-ktx:1.7.2")         // Android activity support
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Constraint layout for flexible UI

    // Navigation components for managing fragment navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    // Firebase dependencies
    implementation("com.google.firebase:firebase-auth-ktx:23.0.0")    // Firebase Authentication for user management
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.0") // Firebase Firestore for database storage
    implementation("com.google.firebase:firebase-storage-ktx:20.2.0")  // Firebase Storage for uploading images/files

    // Glide for efficient image loading and caching
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1") // For Java projects

    // Testing libraries
    testImplementation("junit:junit:4.13.2")                // Unit testing
    androidTestImplementation("androidx.test.ext:junit:1.1.5")  // AndroidX testing
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // Espresso for UI testing

    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.10")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.10")
}
