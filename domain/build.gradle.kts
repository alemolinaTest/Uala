plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlin.serialization)
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