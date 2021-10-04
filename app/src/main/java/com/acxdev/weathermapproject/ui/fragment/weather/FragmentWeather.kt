package com.acxdev.weathermapproject.ui.fragment.weather

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.transition.TransitionManager
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.IDataType.Companion.toDateEpoch
import com.acxdev.commonFunction.util.IDataType.Companion.toPercent
import com.acxdev.commonFunction.util.Toast.Companion.toasty
import com.acxdev.commonFunction.util.view.IRecyclerView.Companion.set
import com.acxdev.weathermapproject.R
import com.acxdev.weathermapproject.adapter.RowWeather
import com.acxdev.weathermapproject.common.BaseFragment
import com.acxdev.weathermapproject.common.Constant
import com.acxdev.weathermapproject.common.CurrentLocation
import com.acxdev.weathermapproject.common.LocationListenerX
import com.acxdev.weathermapproject.databinding.FragmentWeatherBinding
import com.acxdev.weathermapproject.util.setWeatherIcon
import com.acxdev.weathermapproject.util.toCelcius
import com.airbnb.lottie.LottieDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class FragmentWeather : BaseFragment<FragmentWeatherBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentWeatherBinding::inflate

    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            weatherViewModel.getWeather.collect {
                when (it) {
                    is WeatherViewModel.WeatherEvent.Success -> {
                        TransitionManager.beginDelayedTransition(binding.root)
                        binding.date.text = it.weatherOneCallResponse.current.dt.toDateEpoch(Constant.FORMAT_DATE)
                        binding.temp.text = it.weatherOneCallResponse.current.temp.toCelcius()
                        val daily = it.weatherOneCallResponse.daily.toMutableList()
                        daily.removeFirst()
                        binding.weather.set(RowWeather(daily), hasFixed = true)
                        binding.icon.setWeatherIcon(it.weatherOneCallResponse.current.weather[0].description)
                        binding.pressure.text = "${String.format("%.2f", it.weatherOneCallResponse.current.pressure)} hPa"
                        binding.humidity.text = "Humidity: ${it.weatherOneCallResponse.current.humidity.toPercent()}"
                        binding.desc.text = it.weatherOneCallResponse.current.weather[0].description.split(' ').joinToString(" ") { s ->
                            s.capitalize()
                        }
                    }
                    is WeatherViewModel.WeatherEvent.Failure -> {
                        it.message?.let { it1 -> toasty(Toast.ERROR, it1) }
                    }
                    is WeatherViewModel.WeatherEvent.Loading -> {
                        binding.desc.showShimmer()
                        binding.temp.showShimmer()
                        binding.date.showShimmer()
                        binding.pressure.showShimmer()
                        binding.humidity.showShimmer()
                        binding.weather.showShimmer()
                        binding.icon.setAnimation(R.raw.loading)
                        binding.icon.playAnimation()
                        binding.icon.repeatCount = LottieDrawable.INFINITE
                    }
                }
            }
        }

        binding.addCity.setOnClickListener {

        }

        initLocation()
    }

    private fun initLocation() {
        CurrentLocation(requireActivity(), object : LocationListenerX {
            override fun getLocation(location: Location) {
                try {
                    val geoCoder = Geocoder(requireContext()).getFromLocation(
                        location.latitude,
                        location.longitude,
                        10
                    )
                    val address = geoCoder[0]
                    val city = address.subAdminArea
                    val lng = location.longitude
                    val lat = location.latitude
                    binding.location.text = city

                    weatherViewModel.getWeather(lat, lng, Constant.EXCLUDE_OPEN_WEATHER_MAP)
                } catch (e: Exception) {
                    e.localizedMessage?.let { toasty(Toast.ERROR, it) }
                }
            }

        }).getLastLocation()
    }
}