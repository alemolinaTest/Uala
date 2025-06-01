plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(17)
}

dependencies {

    implementation(libs.kotlinx.serialization.json)
    // Kotlin coroutines
    implementation(libs.coroutines.core)

    // DI Annotations
    implementation(libs.javax.inject)

    testImplementation(libs.junit)

}