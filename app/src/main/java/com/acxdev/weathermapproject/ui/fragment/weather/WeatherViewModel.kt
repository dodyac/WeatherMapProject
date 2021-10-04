package com.acxdev.weathermapproject.ui.fragment.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acxdev.jakartaweather.data.model.WeatherOneCallResponse
import com.acxdev.weathermapproject.common.Constant
import com.acxdev.weathermapproject.repository.OpenWeatherMapRepository
import com.acxdev.weathermapproject.util.DispatcherProvider
import com.acxdev.weathermapproject.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: OpenWeatherMapRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler{ _, throwable->
        throwable.printStackTrace()
    }

    sealed class WeatherEvent {
        class Success(val weatherOneCallResponse: WeatherOneCallResponse) : WeatherEvent()
        class Failure(val message: String?) : WeatherEvent()
        object Loading : WeatherEvent()
    }
    private val _getWeather = MutableStateFlow<WeatherEvent>(WeatherEvent.Loading)
    val getWeather: StateFlow<WeatherEvent> = _getWeather

    fun getWeather(lat: Double, lon: Double, exclude: String) {
        viewModelScope.launch(dispatchers.io + exceptionHandler) {
            _getWeather.value = WeatherEvent.Loading
            when (val whatToMineResponse =
                repository.weather(lat, lon, exclude, Constant.API_KEY)) {
                is Resource.Error -> _getWeather.value =
                    WeatherEvent.Failure(whatToMineResponse.message)
                is Resource.Success -> {
                    _getWeather.value = WeatherEvent.Success(whatToMineResponse.data!!)
                }
            }
        }
    }
}