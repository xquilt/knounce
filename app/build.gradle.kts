@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.polendina.knounce"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.polendina.knounce"
        minSdk = 28
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes.addAll(listOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "/META-INF/DEPENDENCIES",
                "mozilla/public-suffix-list.txt"
            ))
        }
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }

}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.junit.ktx)
    val compose_version = "1.5.0"
    implementation("androidx.compose.material:material:${compose_version}")
//    implementation(libs.material3)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.1.0")
    implementation(libs.androidx.core.ktx)

    val lifecycle_version = "2.6.1"
    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // ViewModel utilities for Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.compose.runtime:runtime-livedata:${compose_version}")

    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation (libs.kotlinx.coroutines.test)

    implementation("io.github.torrydo:floating-bubble-view:0.6.4")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation("com.github.therealbush:translator:1.0.2")

    // Junit5
//    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
//    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
//    testImplementation(libs.junit)
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
//    androidTestImplementation(libs.ui.test.junit4)
    testImplementation("org.robolectric:robolectric:4.10.3")
    testImplementation("org.mockito:mockito-core:5.6.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.robolectric:robolectric:4.10.3")
    // Instrumentation testing
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    // Skrapeit
    val skrapeIt = "1.3.0-alpha.1"
    implementation("it.skrape:skrapeit:$skrapeIt") { isChanging = true }
    implementation("it.skrape:skrapeit-http-fetcher:$skrapeIt")
    implementation("it.skrape:skrapeit-async-fetcher:$skrapeIt")

    // Room database
    version = "2.5.2"
    implementation("androidx.room:room-ktx:$version")
    ksp("androidx.room:room-compiler:$version")
    implementation("androidx.room:room-ktx:$version")

    implementation("androidx.fragment:fragment-ktx:1.6.1")

    // Coroutines Testing
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    implementation("com.valentinilk.shimmer:compose-shimmer:1.2.0")

}

//tasks.withType<Test> {
//    useJUnitPlatform()
//}