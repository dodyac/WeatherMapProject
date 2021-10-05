package com.acxdev.weathermapproject.adapter

import com.acxdev.commonFunction.adapter.BaseAdapter
import com.acxdev.commonFunction.util.IDataType.Companion.toDateEpoch
import com.acxdev.weathermapproject.data.model.weather.Daily
import com.acxdev.weathermapproject.common.Constant
import com.acxdev.weathermapproject.databinding.RowWeatherBinding
import com.acxdev.weathermapproject.util.setWeatherIcon
import com.acxdev.weathermapproject.util.toCelcius

class RowWeather(private val list: MutableList<Daily>) :
    BaseAdapter<RowWeatherBinding, Daily>(RowWeatherBinding::inflate, list) {
    override fun onBindViewHolder(holder: ViewHolder<RowWeatherBinding>, position: Int) {
        val list = list[position]
        holder.binding.day.text = list.dt.toDateEpoch(Constant.FORMAT_DAY)
        holder.binding.icon.setWeatherIcon(list.weather[0].description)
        holder.binding.temp.text = list.temp.day.toCelcius()
    }
}