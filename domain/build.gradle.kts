plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(17)
}

dependencies {


    // Kotlin coroutines
    implementation(libs.coroutines.core)

    testImplementation(libs.junit)

}