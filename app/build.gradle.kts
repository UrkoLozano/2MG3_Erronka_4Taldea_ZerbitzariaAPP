plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //kotlin("android") version "<latest_version>"
}

android {
    namespace = "com.example.zerbitzariapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.zerbitzariapp"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildscript {
        dependencies {
            classpath ("com.android.tools.build:gradle:8.10") // Usa la versión más reciente compatible
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3" // Asegúrate de que coincida con las demás dependencias
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    repositories {
        google()
        mavenCentral()
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2023.10.00")) // BOM para consistencia

    // Dependencias principales de Compose
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Dependencias adicionales
    implementation("androidx.navigation:navigation-compose:2.5.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")

    // Herramientas de diseño y pruebas
    androidTestImplementation("androidx.compose.ui:ui-test-junit4") // No necesita versión por el BOM
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Pruebas unitarias
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}