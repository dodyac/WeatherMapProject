package com.acxdev.weathermapproject.repository

import com.acxdev.weathermapproject.data.CityApi
import com.acxdev.weathermapproject.data.model.City
import com.acxdev.weathermapproject.util.Resource
import javax.inject.Inject

class DefaultCityRepository @Inject constructor(
    private val api: CityApi
) : CityRepository {

    override suspend fun getCity(): Resource<List<City>> {
        return try {
            val response = api.getCity()
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