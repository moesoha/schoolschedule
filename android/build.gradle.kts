import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        applicationId = "dev.soha.course202002.schedule.android"
        minSdkVersion(28)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    val versionRoom = "2.3.0"
    val versionAndroidXNavigation = "2.3.5"

    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra.get("versionKotlin")}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
    implementation(project(":model"))

    implementation("androidx.core:core-ktx:1.5.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.annotation:annotation:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.navigation:navigation-fragment-ktx:$versionAndroidXNavigation")
    implementation("androidx.navigation:navigation-ui-ktx:$versionAndroidXNavigation")
    implementation("androidx.navigation:navigation-fragment-ktx:$versionAndroidXNavigation")
    implementation("androidx.navigation:navigation-ui-ktx:$versionAndroidXNavigation")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.preference:preference-ktx:1.1.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("com.android.volley:volley:1.2.0")
    // Room
    implementation("androidx.room:room-runtime:$versionRoom")
    kapt("androidx.room:room-compiler:$versionRoom")
    implementation("androidx.room:room-ktx:$versionRoom")

    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}