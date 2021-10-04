package com.acxdev.weathermapproject.util

import androidx.annotation.StringRes
import com.acxdev.commonFunction.util.IFunction.Companion.getCompatActivity
import com.acxdev.weathermapproject.databinding.LayoutToolbarBinding

object Toolbar {
    fun LayoutToolbarBinding.setToolbar(@StringRes titles: Int) {
        title.apply {
            text = context.getString(titles)
        }
        back.setOnClickListener {
            it.context.getCompatActivity().onBackPressed()
        }
    }

    fun LayoutToolbarBinding.setToolbar(titles: String) {
        title.text = titles
        back.setOnClickListener {
            it.context.getCompatActivity().onBackPressed()
        }
    }
}