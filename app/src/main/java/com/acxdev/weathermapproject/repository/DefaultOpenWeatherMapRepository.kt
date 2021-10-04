package com.acxdev.weathermapproject.repository

import com.acxdev.weathermapproject.data.OpenWeatherMapApi
import com.acxdev.jakartaweather.data.model.WeatherOneCallResponse
import com.acxdev.weathermapproject.util.Resource
import javax.inject.Inject

class DefaultOpenWeatherMapRepository @Inject constructor(
    private val api: OpenWeatherMapApi
) : OpenWeatherMapRepository {

    override suspend fun weather(
        lat: Double,
        lon: Double,
        exclude: String,
        appid: String
    ): Resource<WeatherOneCallResponse> {
        return try {
            val response = api.getWeatherOneCall(lat, lon, exclude, appid)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occured")
        }
    }
}