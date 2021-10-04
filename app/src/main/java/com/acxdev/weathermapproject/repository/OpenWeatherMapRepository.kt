package com.acxdev.weathermapproject.repository

import com.acxdev.weathermapproject.data.model.weather.WeatherOneCallResponse
import com.acxdev.weathermapproject.util.Resource

interface OpenWeatherMapRepository {

    suspend fun weather(
        lat: Double,
        lon: Double,
        exclude: String,
        appid: String
    ): Resource<WeatherOneCallResponse>
}