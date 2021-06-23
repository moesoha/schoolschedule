// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val versionKotlin by rootProject.extra { "1.5.10" }

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${rootProject.extra.get("versionKotlin")}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${rootProject.extra.get("versionKotlin")}")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    group = "dev.soha.course202002.schedule"
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}