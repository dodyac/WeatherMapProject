package com.acxdev.weathermapproject.util

import android.content.Intent
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import com.acxdev.commonFunction.common.Language
import com.acxdev.commonFunction.util.view.ITextInputLayout.Companion.alertEmpty
import com.acxdev.weathermapproject.R
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.isNotEmpty(): Boolean = alertEmpty(Language.ID)

fun TextInputLayout.toEditString(): String = editText?.text.toString()

fun TextInputLayout.isNotEmptyWithMinimum(char: Int): Boolean {
    return when {
        editText!!.text.isEmpty() -> {
            isErrorEnabled = true
            error = "$hint tidak boleh kosong!"
            requestFocus()
            false
        }
        editText!!.text.toString().length < char -> {
            isErrorEnabled = true
            error = "$hint minimal $char karakter!"
            requestFocus()
            false
        }
        else -> {
            isErrorEnabled = false
            clearFocus()
            true
        }
    }
}

fun TextInputLayout.setText(string: String?){
    editText?.setText(string)
}


fun initIntentResult(resultLauncher: ActivityResultLauncher<Intent>){
    Intent(
        Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    ).also {
        resultLauncher.launch(it)
    }
}

fun LottieAnimationView.setWeatherIcon(description: String) {
    when (description) {
        "broken clouds" -> setAnimation(R.raw.broken_clouds)
        "light rain" -> setAnimation(R.raw.light_rain)
        "overcast clouds" -> setAnimation(R.raw.overcast_clouds)
        "moderate rain" -> setAnimation(R.raw.moderate_rain)
        "few clouds" -> setAnimation(R.raw.few_clouds)
        "heavy intensity rain" -> setAnimation(R.raw.heavy_intentsity)
        "clear sky" -> setAnimation(R.raw.clear_sky)
        "scattered clouds" -> setAnimation(R.raw.scattered_clouds)
        "fog" -> setAnimation(R.raw.broken_clouds)
        else -> setAnimation(R.raw.unknown)
    }
    playAnimation()
    repeatCount = LottieDrawable.INFINITE
}

fun Double.toCelcius(): String {
    val result = minus(273.15)
    return "${String.format("%.2f", result)} °C"
}
