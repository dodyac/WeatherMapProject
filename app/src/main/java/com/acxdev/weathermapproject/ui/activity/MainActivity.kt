package com.acxdev.weathermapproject.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.adapter.ViewPager2Adapter
import com.acxdev.weathermapproject.R
import com.acxdev.weathermapproject.common.BaseActivity
import com.acxdev.weathermapproject.databinding.ActivityMainBinding
import com.acxdev.weathermapproject.ui.fragment.FragmentProfile
import com.acxdev.weathermapproject.ui.fragment.weather.FragmentWeather
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pager2Adapter = ViewPager2Adapter(this)
        val mutableListFragment = mutableListOf<Pair<String, Fragment>>()
        mutableListFragment.add(Pair(getString(R.string.cuaca), FragmentWeather()))
        mutableListFragment.add(Pair(getString(R.string.profil), FragmentProfile()))
        binding.viewPager.adapter = pager2Adapter
        pager2Adapter.setWithTab(mutableListFragment, binding.tabLayout, binding.viewPager)

    }

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = ActivityMainBinding::inflate
}