plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra.get("versionKotlin")}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
}