plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.askolds.homeinventory"
    compileSdk = 34

    androidResources {
        generateLocaleConfig = true
    }

    defaultConfig {
        applicationId = "com.askolds.homeinventory"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type. Make sure to use a build
            // variant with `isDebuggable=false`.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar)
    implementation(libs.core.ktx)

    // Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.hilt.navigation.compose)

    androidTestImplementation (libs.hilt.testing)
    kspAndroidTest(libs.hilt.compiler)


    // Compose
    implementation(platform(compose.bom))
    implementation(compose.foundation)
    implementation(compose.ui)
    implementation(compose.ui.graphics)
    implementation(compose.ui.tooling.preview)

    implementation(compose.material3)
    implementation(compose.material.icons)

    debugImplementation(compose.ui.tooling)
    debugImplementation(compose.ui.test.manifest)

    androidTestImplementation(platform(compose.bom))
    androidTestImplementation(compose.ui.test.junit4)

    implementation(compose.navigation)

    //

    implementation(libs.activity.compose)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.coroutines)

    // Room
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    // Coil
    implementation(libs.coil)

    // test
    testImplementation(libs.junit)

    // androidTest
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}

ksp {
    arg("room.generateKotlin", "true")
    arg("room.schemaLocation", "$projectDir/schemas")
}