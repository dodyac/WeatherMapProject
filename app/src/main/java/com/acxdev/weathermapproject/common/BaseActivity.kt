package com.acxdev.weathermapproject.common

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.util.IFunction.Companion.useCurrentTheme

abstract class BaseActivity<out VB : ViewBinding> : AppCompatActivity() {

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    protected val gone: Int = View.GONE
    protected val visible: Int = View.VISIBLE


    override fun onCreate(savedInstanceState: Bundle?) {
        useCurrentTheme()
        super.onCreate(savedInstanceState)
        _binding = bindingInflater(layoutInflater)
        setContentView(binding.root)
    }

    protected abstract val bindingInflater: (LayoutInflater) -> ViewBinding

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 1.0f
        applyOverrideConfiguration(newOverride)
    }

    override fun onResume() {
        useCurrentTheme()
        super.onResume()
    }
}