package com.acxdev.weathermapproject.repository

import com.acxdev.weathermapproject.data.model.City
import com.acxdev.weathermapproject.util.Resource

interface CityRepository {

    suspend fun getCity(): Resource<List<City>>
}