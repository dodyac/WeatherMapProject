package com.acxdev.jakartaweather.data.model

data class Daily(
    val dt: Long,
    val sunrise: Double,
    val sunset: Double,
    val moonrise: Double,
    val moonset: Double,
    val moon_phase: Double,
    val temp: Temp,
    val feels_like: FeelsLike,
    val pressure: Double,
    val humidity: Double,
    val dew_point: Double,
    val wind_speed: Double,
    val wind_deg: Double,
    val wind_gust: Double,
    val weather: List<Weather>,
    val clouds: Double,
    val pop: Double,
    val uvi: Double,
    val rain: Double? = null
)