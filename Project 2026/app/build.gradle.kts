plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.spacecolony"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.spacecolony"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Core Android stuff
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // RecyclerView for listing crew members
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Gson for saving/loading crew data as JSON
    implementation("com.google.code.gson:gson:2.10.1")

    // MPAndroidChart for statistics visualization
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Testing (leave these as default)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}