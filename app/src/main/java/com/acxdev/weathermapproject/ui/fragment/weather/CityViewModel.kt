package com.acxdev.weathermapproject.ui.fragment.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acxdev.weathermapproject.data.model.City
import com.acxdev.weathermapproject.repository.CityRepository
import com.acxdev.weathermapproject.util.DispatcherProvider
import com.acxdev.weathermapproject.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    private val repository: CityRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    sealed class CityEvent {
        class Success(val list: List<City>) : CityEvent()
        class Failure(val message: String?) : CityEvent()
        object Loading : CityEvent()
    }

    private val _getCity = MutableStateFlow<CityEvent>(CityEvent.Loading)
    val getCity: StateFlow<CityEvent> = _getCity

    fun getCity() {
        viewModelScope.launch(dispatchers.io + exceptionHandler) {
            _getCity.value = CityEvent.Loading
            when (val response =
                repository.getCity()) {
                is Resource.Error -> _getCity.value =
                    CityEvent.Failure(response.message)
                is Resource.Success -> {
                    _getCity.value = CityEvent.Success(response.data!!)
                }
            }
        }
    }
}