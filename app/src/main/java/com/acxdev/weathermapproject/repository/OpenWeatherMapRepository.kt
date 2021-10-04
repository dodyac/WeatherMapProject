package com.acxdev.weathermapproject.repository

import com.acxdev.jakartaweather.data.model.WeatherOneCallResponse
import com.acxdev.weathermapproject.util.Resource

interface OpenWeatherMapRepository {

    suspend fun weather(
        lat: Double,
        lon: Double,
        exclude: String,
        appid: String
    ): Resource<WeatherOneCallResponse>
}