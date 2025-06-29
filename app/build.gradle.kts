plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.surajvanshsv.alarmapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.surajvanshsv.alarmapp"
        minSdk = 24
        targetSdk = 35
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
    implementation (libs.room.runtime)
    annotationProcessor (libs.room.compiler)
    implementation (libs.lifecycle.livedata.ktx)
    implementation (libs.material.v1110)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}