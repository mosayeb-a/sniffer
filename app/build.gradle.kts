import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Base64
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexplicit-backing-fields")
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

android {
    namespace = "com.ma.sniffer"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.ma.sniffer"
        minSdk = 23
        targetSdk = 37
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        val keystoreProperties = Properties()
        val keystoreFile = project.rootProject.file("keystore.properties")
        if (keystoreFile.exists()) {
            keystoreProperties.load(keystoreFile.inputStream())
        }

        val keyAlias = keystoreProperties.getProperty("keyAlias")
        val storePassword = keystoreProperties.getProperty("storePassword")
        val keyPassword = keystoreProperties.getProperty("keyPassword")
        val keystoreBase64 = keystoreProperties.getProperty("keystoreBase64")

        if (
            !keyAlias.isNullOrBlank() &&
            !storePassword.isNullOrBlank() &&
            !keyPassword.isNullOrBlank() &&
            !keystoreBase64.isNullOrBlank()
        ) {
            create("release") {
                this.keyAlias = keyAlias
                this.storePassword = storePassword
                this.keyPassword = keyPassword

                val keystoreBytes = Base64.getDecoder().decode(keystoreBase64)
                val tempKeystore = File("${project.buildDir}/temp_keystore.jks")
                tempKeystore.parentFile.mkdirs()
                tempKeystore.writeBytes(keystoreBytes)
                storeFile = tempKeystore
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfigs.findByName("release")?.let { signingConfig = it }
        }

        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.material3.android)
    implementation(libs.material.icons.extended)

    implementation(libs.koin.compose)
    implementation(libs.koin.android)

    implementation(libs.datastore.preferences)

    implementation(libs.kotlinx.serialization)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
}