package com.acxdev.jakartaweather.data.model

data class WeatherOneCallResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Long,
    val current: Current,
    val minutely: List<Minutely>,
    val hourly: List<Current>,
    val daily: List<Daily>
)