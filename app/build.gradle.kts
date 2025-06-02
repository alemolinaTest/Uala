import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.amolina.uala"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.amolina.uala"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // Load secrets.properties explicitly
        val secretsFile = rootProject.file("secrets.properties")
        if (secretsFile.exists()) {
            val secrets = Properties()
            secrets.load(FileInputStream(secretsFile))
            manifestPlaceholders["GOOGLE_MAPS_API_KEY"] = secrets["GOOGLE_MAPS_API_KEY"] ?: ""
        } else {
            throw GradleException("secrets.properties file not found!")
        }

        println("Google Maps API Key: ${manifestPlaceholders["GOOGLE_MAPS_API_KEY"]}")

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf("-Xlint:deprecation")
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    //  Modules
    implementation(project(":presentation"))
    implementation(project(":domain"))
    implementation(project(":data"))

    //  Core Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.okhttp.logging)

    //  Navigation
    implementation(libs.navigation.compose)

    //  Dependency Injection (Koin)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)

    //  Testing Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    //  Debug Libraries
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}