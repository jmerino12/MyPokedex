plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.tag.infrastructure"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":feature:auth:domain"))
    implementation(project(":feature:pokemon:domain"))
    implementation(project(":feature:tag:domain"))
    implementation(project(":feature:pokemon:infrastructure"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.corrutines)
    implementation(platform(libs.firebaseBom))
    implementation(libs.firebase.store)
    implementation(libs.room.ktx)


    testImplementation(libs.junit)
    testImplementation(libs.mockito.kotlin)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.androidx.room.testing)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation("org.mockito:mockito-android:5.12.0")
    androidTestImplementation (libs.kotlinx.coroutines.test)
}