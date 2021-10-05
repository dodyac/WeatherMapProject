package com.acxdev.weathermapproject.ui.fragment.weather

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
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
import com.acxdev.weathermapproject.data.model.City
import com.acxdev.weathermapproject.data.model.weather.Daily
import com.acxdev.weathermapproject.databinding.FragmentWeatherBinding
import com.acxdev.weathermapproject.util.setItem
import com.acxdev.weathermapproject.util.setWeatherIcon
import com.acxdev.weathermapproject.util.toCelcius
import com.acxdev.weathermapproject.util.toCelciusRaw
import com.airbnb.lottie.LottieDrawable
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class FragmentWeather : BaseFragment<FragmentWeatherBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentWeatherBinding::inflate

    private val weatherViewModel: WeatherViewModel by viewModels()
    private val cityViewModel: CityViewModel by viewModels()
    private lateinit var listCity: List<City>

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


                        //Chart
                        val dailyChart: List<Daily> = it.weatherOneCallResponse.daily

                        val listTemp = dailyChart.map { day -> day.temp.day.toFloat() }
                        val listDewPoint = dailyChart.map { day -> day.dew_point.toFloat() }
                        val listHumidity = dailyChart.map { day -> day.humidity.toFloat() }
                        val timeList = dailyChart.map { day -> day.dt.toDateEpoch(Constant.FORMAT_DATE_CHART, Locale.ENGLISH) }


                        val temp = lineDataSetFilledDrawableWithReadable(getString(R.string.temp), listTemp, R.color.green, R.drawable.bg_fade_green)
                        val dewPoint = lineDataSetFilledDrawableWithReadable(getString(R.string.dewPoint), listDewPoint, R.color.blue, R.drawable.bg_fade_blue)
                        val humidity = lineDataSetFilledDrawableWithReadable(getString(R.string.humidity), listHumidity, R.color.yellow, R.drawable.bg_fade_yellow, false)

                        val dataset = mutableListOf<ILineDataSet>()
                        dataset.add(temp)
                        dataset.add(dewPoint)
                        dataset.add(humidity)
                        binding.chart.applyStyle(timeList, dataset)

                        binding.tempCheck.setOnCheckedChangeListener { _, isChecked ->
                            if(isChecked) dataset.add(temp) else dataset.remove(temp)
                            binding.chart.invalidate()
                        }

                        binding.dewPointCheck.setOnCheckedChangeListener { _, isChecked ->
                            if(isChecked) dataset.add(dewPoint) else dataset.remove(dewPoint)
                            binding.chart.invalidate()
                        }

                        binding.humidityCheck.setOnCheckedChangeListener { _, isChecked ->
                            if(isChecked) dataset.add(humidity) else dataset.remove(humidity)
                            binding.chart.invalidate()
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

        lifecycleScope.launchWhenStarted {
            cityViewModel.getCity.collect{
                when (it) {
                    is CityViewModel.CityEvent.Success -> {
                        binding.searchTil.isStartIconVisible = false
                        binding.search.setItem(it.list.map { city -> city.city })
                        listCity = it.list
                        binding.search.setOnItemClickListener { parent, _, position, _ ->
                            val selection = parent.getItemAtPosition(position) as String
                            for(city in listCity) if(city.city == selection) {
                                binding.location.text = city.city
                                weatherViewModel.getWeather(city.latitude, city.longitude, Constant.EXCLUDE_OPEN_WEATHER_MAP)
                            }
                        }
                    }
                    is CityViewModel.CityEvent.Failure -> {
                        binding.searchTil.isStartIconVisible = false
                        it.message?.let { it1 -> toasty(Toast.ERROR, it1) }
                    }
                    is CityViewModel.CityEvent.Loading -> {
                        binding.searchTil.isStartIconVisible = true
                    }
                }
            }
        }

        cityViewModel.getCity()
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

    private fun lineDataSetFilledDrawableWithReadable(
        label: String,
        list: List<Float?>,
        color: Int,
        filledColor: Int,
        toCelcius: Boolean = true
    ): ILineDataSet {
        val entry = mutableListOf<Entry>()
        for(i in list.indices) entry.add(Entry(i.toFloat(),
            try {
                if (toCelcius) list[i]!!.toFloat().toDouble().toCelciusRaw()
                else list[i]!!.toFloat()
            } catch (e: Exception){
                0F
            })
        )
        val lineDataSet = LineDataSet(entry, label)
        lineDataSet.color = requireContext().getColor(color)
        lineDataSet.lineWidth = 2F
        lineDataSet.setDrawCircles(false)
        lineDataSet.fillDrawable = AppCompatResources.getDrawable(requireContext(), filledColor)
        lineDataSet.setDrawFilled(true)
        lineDataSet.valueTextSize = 0F
        lineDataSet.highLightColor = requireContext().getColor(android.R.color.transparent)
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        return lineDataSet
    }

    private fun LineChart.applyStyle(indexAxisValueFormatter: List<String>, dataset: MutableList<ILineDataSet>){
        legend.isEnabled = false
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)

        xAxis.valueFormatter = IndexAxisValueFormatter(indexAxisValueFormatter)
        xAxis.granularity = 1F
        xAxis.isGranularityEnabled = true
        axisRight.isEnabled = false
        axisLeft.textColor = context.getColor(R.color.text)
        axisRight.textColor = context.getColor(R.color.text)
        xAxis.textColor = context.getColor(R.color.text)

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        animateX(600, Easing.Linear)

        axisRight.enableGridDashedLine(10F, 10F, 0F)
        axisLeft.enableGridDashedLine(10F, 10F, 0F)
        xAxis.setDrawGridLines(false)
        axisLeft.setDrawAxisLine(false)
        axisRight.setDrawAxisLine(false)
        data = LineData(dataset)
        description.isEnabled = false
        invalidate()
    }
}