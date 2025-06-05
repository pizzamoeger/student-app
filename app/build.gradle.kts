plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.hannah.studentapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.hannah.studentapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 3
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(platform(libs.firebase.bom)) // <-- Use the latest version number here! Check Firebase docs.

    // Add the dependency for the Firebase Authentication library (Kotlin version)
    implementation(libs.com.google.firebase.firebase.auth.ktx)

    // Add the dependency for FirebaseUI Auth (needed for AuthUI and the Contract)
    implementation(libs.firebase.ui.auth) // <-- Use the latest version number here! Check FirebaseUI docs.

    implementation(libs.firebase.messaging)
    implementation (libs.androidx.core.splashscreen)
    implementation(libs.gson)
    implementation(libs.mpandroidchart)
    implementation (libs.colorpicker)
    implementation (libs.jaredrummler.colorpicker)
    implementation (libs.material.v1110)
    implementation(libs.flexbox)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.ui.auth)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.navigation.fragment.ktx.v277)
    implementation(libs.androidx.navigation.ui.ktx.v277)
}