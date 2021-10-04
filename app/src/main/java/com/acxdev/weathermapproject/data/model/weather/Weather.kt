package com.acxdev.weathermapproject.data.model.weather

data class Weather(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)