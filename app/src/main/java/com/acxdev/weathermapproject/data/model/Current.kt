package com.acxdev.jakartaweather.data.model

data class Current(
    val dt: Double,
    val sunrise: Double? = null,
    val sunset: Double? = null,
    val temp: Double,
    val feels_like: Double,
    val pressure: Double,
    val humidity: Double,
    val dew_point: Double,
    val uvi: Double,
    val clouds: Double,
    val visibility: Double,
    val wind_speed: Double,
    val wind_deg: Double,
    val wind_gust: Double,
    val weather: List<Weather>,
    val pop: Double? = null,
    val rain: Rain? = null
)