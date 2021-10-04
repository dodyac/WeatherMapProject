import Libraries.androidTestImplementationX
import Libraries.implementationX
import Libraries.kaptX
import Libraries.testImplementationX

plugins {
    id(Config.Plugins.android)
    id(Config.Plugins.daggerHilt)
    kotlin(Config.Plugins.kotlinAndroid)
    kotlin(Config.Plugins.kotlinKapt)
}

android {
    compileSdkVersion(Versions.compileSdk)
    buildToolsVersion(Versions.buildTool)

    defaultConfig {
        applicationId = ApplicationId.appId
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = Config.testRunner
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions.jvmTarget = Versions.jvm
    lintOptions.disable("ContentDescription")
}

dependencies {
    implementationX(Libraries.appLibraries)
    implementation("com.google.android.play:core-ktx:1.8.1")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    kaptX(Libraries.kaptLibraries)
    coreLibraryDesugaring(Libraries.desugar)
//    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.7")
//    testImplementation("com.squareup.leakcanary:leakcanary-android-no-op:2.7")
    androidTestImplementationX(Libraries.androidTestLibraries)
    testImplementationX(Libraries.testLibraries)
}