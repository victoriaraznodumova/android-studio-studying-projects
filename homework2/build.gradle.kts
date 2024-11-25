
buildscript{
    repositories{
        mavenCentral()
    }
    dependencies {
        classpath("io.realm:realm-gradle-plugin:10.14.0-transformer-api")
    }
}


// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}