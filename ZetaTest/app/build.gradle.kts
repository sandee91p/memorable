plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    signingConfigs.create("Zeta")
    signingConfigs {
        getByName("Zeta") {
            storeFile = file("Zeta.jks")
            storePassword = "1234567890"
            keyAlias = "key0"
            keyPassword = "1234567890"
        }
    }
    ndkVersion = (rootProject.ext["ndkVersion"] as String)
    namespace = (rootProject.ext["packagename"] as String)
    compileSdk = (rootProject.ext["compileSdk"] as Int)
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                argument("includeCompileClasspath", "false")
            }
        }

        applicationId = (rootProject.ext["appid"] as String)
        minSdk = (rootProject.ext["minSdk"] as Int)
        //noinspection ExpiredTargetSdkVersion
        targetSdk = (rootProject.ext["targetSdk"] as Int)
        versionCode = (rootProject.ext["versioncode"] as Int)
        versionName = (rootProject.ext["appversion"] as String)
        vectorDrawables {
            useSupportLibrary = true
        }
        multiDexEnabled = false
    }
    buildTypes {
        release {
            signingConfigs.apply {
                signingConfigs.getByName("Zeta"){
                    enableV1Signing = true
                    enableV2Signing = true
                    enableV3Signing = true
                    enableV4Signing = true
                }
            }
            resValue ("string", "app_name", "ZXTest-Release")
            isMinifyEnabled = true // not tested yet
            isJniDebuggable = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            signingConfigs.getByName("debug"){
               enableV1Signing = true
               enableV2Signing = true
               enableV3Signing = true
               enableV4Signing = true
            }
            isJniDebuggable = false
            isDebuggable = false
            resValue ("string", "app_name", "ZXTest-Debug")
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
    buildFeatures{
        buildConfig = true
        viewBinding = true

    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        aidl = true
        prefab = true
        viewBinding = true
    }

   splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a") //"armeabi-v7a",
            isUniversalApk = false
        }
    }
}

val ktxversion = rootProject.ext["ktx_version"] as String
val stdlib_version = rootProject.ext["stdlib_version"] as String
val googlematerial = rootProject.ext["googlematerial"] as String

dependencies {
    implementation(project(":ZetaBinder"))
    implementation("androidx.core:core-ktx:$ktxversion")
    //noinspection GradleDependency
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$stdlib_version")



}