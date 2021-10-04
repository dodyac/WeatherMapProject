package com.acxdev.weathermapproject.ui

import android.app.Dialog
import android.content.Context
import com.acxdev.commonFunction.util.IFunction.Companion.openSettings
import com.acxdev.commonFunction.util.IFunction.Companion.useCurrentTheme
import com.acxdev.weathermapproject.BuildConfig
import com.acxdev.weathermapproject.databinding.DialogCustomBinding

class Custom(context: Context) : Dialog(context){
    private var binding: DialogCustomBinding
    init {
        context.useCurrentTheme()
        binding = DialogCustomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.done.setOnClickListener {
            context.openSettings(BuildConfig.APPLICATION_ID)
            dismiss()
        }
        binding.cancel.setOnClickListener { dismiss() }
    }
}
