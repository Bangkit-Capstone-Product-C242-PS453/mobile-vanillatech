plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id ("kotlin-parcelize")
}

android {
    namespace = "umi.app.vantech"
    compileSdk = 34

    defaultConfig {
        applicationId = "umi.app.vantech"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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

    buildFeatures{
        viewBinding = true
        mlModelBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation (libs.glide)
    implementation (libs.circleimageview)

    implementation (libs.circularprogressbar)
    implementation (libs.lottie)
    implementation (libs.circleimageview)
    implementation (libs.glide)
    implementation (libs.androidx.viewpager2)
    implementation (libs.dotsindicator)
        // Retrofit
    implementation (libs.retrofit)
        // Converter untuk JSON (misalnya, Gson)
    implementation (libs.retrofit2.converter.gson)

        // OkHttp
    implementation (libs.okhttp)
        // OkHttp Logging Interceptor (opsional, untuk logging)
    implementation (libs.logging.interceptor)
    // ViewModel dan LiveData
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)

    // Jika Anda menggunakan ViewModel dan LiveData dengan coroutines
    implementation (libs.androidx.lifecycle.runtime.ktx)

    implementation (libs.tensorflow.lite)
    implementation (libs.tensorflow.lite.gpu)
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.7.10") // Ganti dengan versi Kotlin yang Anda gunakan




}