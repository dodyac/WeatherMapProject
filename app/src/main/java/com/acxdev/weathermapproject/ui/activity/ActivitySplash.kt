package com.acxdev.weathermapproject.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.acxdev.commonFunction.util.Preference.Companion.isLogged

class ActivitySplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(isLogged()){
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        } else {
            Intent(this, ActivitySignIn::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }
}
