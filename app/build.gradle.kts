plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.projedeneme"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.projedeneme"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")

    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:1.1.5")
    implementation ("androidx.multidex:multidex:2.0.1")
    implementation ("org.jsoup:jsoup:1.10.3")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation ("com.google.code.gson:gson:2.8.6")
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")

    implementation ("com.google.android.gms:play-services-maps:17.0.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.android.libraries.places:places:2.4.0")
    implementation ("com.android.volley:volley:1.2.1")
}