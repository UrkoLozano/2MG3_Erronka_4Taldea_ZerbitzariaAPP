    plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.kotlin.android)
        id("kotlin-kapt") // Plugin para Room (compilador)

    }

    android {
        namespace = "com.example.myapplication"
        compileSdk = 35

        defaultConfig {
            applicationId = "com.example.myapplication"
            minSdk = 25
            targetSdk = 34
            versionCode = 1
            versionName = "1.0"

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables {
                useSupportLibrary = true
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
            kotlinCompilerExtensionVersion = "1.5.1"
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
           }

    dependencies {

        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)
        implementation(libs.androidx.navigation.compose)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)
        implementation("androidx.compose.ui:ui:1.7.6") // Compose
        implementation("androidx.room:room-runtime:2.6.1") // Room
        implementation("androidx.room:room-ktx:2.6.1") // Room
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")
        implementation(kotlin("script-runtime"))
        implementation("androidx.navigation:navigation-fragment-ktx:2.8.5")
        implementation("androidx.navigation:navigation-ui-ktx:2.8.5")
        implementation("com.google.dagger:dagger-android-support:2.55")
        kapt("com.google.dagger:dagger-android-processor:2.55")
        kapt("com.google.dagger:dagger-compiler:2.55")
        kapt("androidx.room:room-compiler:2.6.1")
        implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")



    }