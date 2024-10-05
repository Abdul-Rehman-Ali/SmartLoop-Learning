plugins {
    id("kotlin-kapt") // Ensure this is included
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.smartloopLearn.learning"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.smartloopLearn.learning"
        minSdk = 24
        targetSdk = 34
        versionCode = 4
        versionName = "4.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
    }
    packagingOptions {
        resources {
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "META-INF/DEPENDENCIES",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/DEPENDENCIES.txt"
            )
        }
    }
}

dependencies {
    implementation("com.github.barteksc:AndroidPdfViewerV1:1.6.0")
    implementation(libs.circleimageview)
    implementation(libs.lottie)
    implementation(libs.imageslideshow)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.junit.jupiter)
    implementation(kotlin("script-runtime"))
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.firestore)

    // Glide dependencies
    implementation("com.github.bumptech.glide:glide:4.15.1") // Glide
    kapt("com.github.bumptech.glide:compiler:4.15.1") // Glide compiler
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.junit.jupiter") {
            // Add an exclusion for the specific license file causing the conflict
            exclude(module = "META-INF/LICENSE.md")
        }
    }
}
