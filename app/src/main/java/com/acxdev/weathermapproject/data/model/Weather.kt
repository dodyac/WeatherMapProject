package com.acxdev.jakartaweather.data.model

data class Weather(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)