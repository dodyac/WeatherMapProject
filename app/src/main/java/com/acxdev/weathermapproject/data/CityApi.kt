package com.acxdev.weathermapproject.data

import com.acxdev.weathermapproject.data.model.City
import retrofit2.Response
import retrofit2.http.GET

interface CityApi {
    @GET("Miserlou/c5cd8364bf9b2420bb29/raw/2bf258763cdddd704f8ffd3ea9a3e81d25e2c6f6/cities.json")
    suspend fun getCity(): Response<List<City>>
}