import org.gradle.api.artifacts.dsl.DependencyHandler

object Libraries {
    //Android
    private const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    private const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    private const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    private const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    private const val legacySupport = "androidx.legacy:legacy-support-v4:${Versions.legacySupport}"
    private const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.fragmentKtx}"
    private const val testJunit = "androidx.test.ext:junit:${Versions.testJunit}"
    private const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    private const val jUnit = "junit:junit:${Versions.jUnit}"
    const val desugar = "com.android.tools:desugar_jdk_libs:${Versions.desugar}"
    const val serviceLocation = "com.google.android.gms:play-services-location:${Versions.serviceLocation}"
    // UI library
    private const val material = "com.google.android.material:material:${Versions.material}"
    private const val circleImage = "de.hdodenhof:circleimageview:${Versions.circleImage}"
    private const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"
    // Network client
    private const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    private const val retrofitGson  = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    private const val logging  = "com.squareup.okhttp3:logging-interceptor:${Versions.logging}"
    //Others
    private const val dexter = "com.karumi:dexter:${Versions.dexter}"
    private const val commonFunction = "com.github.dodyac:commonFunction:${Versions.commonFunction}"
    private const val shimmerView = "com.github.dodyac:ShimmerView:${Versions.shimmerView}"
    private const val materialDialog = "dev.shreyaspatil.MaterialDialog:MaterialDialog:${Versions.materialDialog}"
    //Dagger - Hilt
    private const val daggerHilt = "com.google.dagger:hilt-android:${Versions.daggerHiltLib}"
    private const val daggerHiltSupport = "com.google.dagger:dagger-android-support:${Versions.daggerHiltLib}"
    private const val daggerHiltKapt = "com.google.dagger:dagger-android-processor:${Versions.daggerHiltLib}"
    private const val daggerHiltCompilerKapt = "com.google.dagger:hilt-android-compiler:${Versions.daggerHiltLib}"
    private const val hiltLifeCycle = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hiltLifeCycle}"
    private const val hiltLifeCycleKapt = "androidx.hilt:hilt-compiler:${Versions.hiltLifeCycleKapt}"
    // Activity KTX for viewModels()
    private const val ktxViewModels = "androidx.activity:activity-ktx:${Versions.ktxViewModels}"
    // Lifecycle
    private const val lifeCycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifeCycle}"
    private const val lifeCycleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifeCycle}"
    private const val lifeCycleViewModel =  "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifeCycle}"
    private const val ktxViewModelsLifecycle =  "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifeCycle}"
    // Coroutines
    private const val kotlinCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
    private const val kotlinCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"
    //Room
    private const val room = "androidx.room:room-runtime:${Versions.room}"
    private const val roomKapt = "androidx.room:room-compiler:${Versions.room}"
    // Kotlin Extensions and Coroutines support for Room
    private const val roomKtx =  "androidx.room:room-ktx:${Versions.room}"

    val appLibraries = arrayListOf<String>().apply {
        add(kotlinStdlib)
        add(coreKtx)
        add(appCompat)
        add(constraintLayout)
        add(legacySupport)
        add(fragmentKtx)
        add(material)
        add(circleImage)
        add(serviceLocation)
        add(lottie)
        add(retrofit)
        add(retrofitGson)
        add(logging)
        add(materialDialog)
        add(dexter)
        add(retrofit)
        add(commonFunction)
        add(shimmerView)
        add(daggerHilt)
        add(daggerHiltSupport)
        add(hiltLifeCycle)
        add(ktxViewModels)
        add(lifeCycleExtensions)
        add(lifeCycleLiveData)
        add(lifeCycleViewModel)
        add(ktxViewModelsLifecycle)
        add(kotlinCoroutinesCore)
        add(kotlinCoroutinesAndroid)
        add(room)
        add(roomKtx)
    }

    val testLibraries = arrayListOf<String>().apply {
        add(jUnit)
    }

    val androidTestLibraries = arrayListOf<String>().apply {
        add(testJunit)
        add(espressoCore)
    }

    val kaptLibraries = arrayListOf<String>().apply {
        add(roomKapt)
        add(daggerHiltKapt)
        add(hiltLifeCycleKapt)
        add(daggerHiltCompilerKapt)
    }

    fun DependencyHandler.implementationX(list: List<String>) {
        list.forEach { dependency -> add("implementation", dependency) }
    }

    fun DependencyHandler.kaptX(list: List<String>) {
        list.forEach { dependency -> add("kapt", dependency) }
    }

    fun DependencyHandler.androidTestImplementationX(list: List<String>) {
        list.forEach { dependency -> add("androidTestImplementation", dependency) }
    }

    fun DependencyHandler.testImplementationX(list: List<String>) {
        list.forEach { dependency -> add("testImplementation", dependency) }
    }
}