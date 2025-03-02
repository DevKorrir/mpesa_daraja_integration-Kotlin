import java.util.Properties
import java.io.FileInputStream



plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
}

fun getLocalProperty(key: String, defaultValue: String = ""): String {
    val properties = Properties()
    val localPropertiesFile = File(rootProject.projectDir, "local.properties")
    if (localPropertiesFile.exists()) {
        FileInputStream(localPropertiesFile).use { properties.load(it) }
    }
    return properties.getProperty(key, defaultValue)
}

android {
    namespace = "dev.korryr.mpesaapi"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.korryr.mpesaapi"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "CONSUMER_KEY", "\"${getLocalProperty("mpesa.consumerKey")}\"")
        buildConfigField("String", "CONSUMER_SECRET", "\"${getLocalProperty("mpesa.consumerSecret")}\"")
        buildConfigField("String", "PASSKEY", "\"${getLocalProperty("mpesa.passkey")}\"")
        buildConfigField("String", "CALLBACK_URL", "\"${getLocalProperty("mpesa.callbackUrl")}\"")
        buildConfigField("String", "BUSINESS_SHORT_CODE", "\"${getLocalProperty("mpesa.businessShortCode")}\"")
        buildConfigField ("String", "MPESA_BASE_URL", "\"https://sandbox.safaricom.co.ke/\"")



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
    buildFeatures {
        compose = true
        buildConfig = true
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
    implementation(libs.androidx.runtime.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Retrofit for API calls
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    // Hilt for dependency injection
    implementation (libs.hilt.android)
    kapt (libs.hilt.android.compiler)

    // ViewModel with Hilt
    implementation (libs.androidx.hilt.navigation.compose)

    implementation (libs.logging.interceptor)



}