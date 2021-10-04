package com.acxdev.weathermapproject.di

import com.acxdev.weathermapproject.data.OpenWeatherMapApi
import com.acxdev.weathermapproject.common.Constant
import com.acxdev.weathermapproject.data.CityApi
import com.acxdev.weathermapproject.repository.CityRepository
import com.acxdev.weathermapproject.repository.DefaultCityRepository
import com.acxdev.weathermapproject.repository.DefaultOpenWeatherMapRepository
import com.acxdev.weathermapproject.repository.OpenWeatherMapRepository
import com.acxdev.weathermapproject.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private fun retrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BASIC)
                )
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        ).build()

    @Singleton
    @Provides
    fun openWeatherMapApi(): OpenWeatherMapApi = retrofit(Constant.BASE_URL_OPEN_WEATHER).create(OpenWeatherMapApi::class.java)

    @Singleton
    @Provides
    fun provideOpenWeatherMapRepository(api: OpenWeatherMapApi): OpenWeatherMapRepository =
        DefaultOpenWeatherMapRepository(api)

    @Singleton
    @Provides
    fun cityApi(): CityApi = retrofit(Constant.BASE_URL_CITY).create(CityApi::class.java)

    @Singleton
    @Provides
    fun provideCityRepository(api: CityApi): CityRepository =
        DefaultCityRepository(api)

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}