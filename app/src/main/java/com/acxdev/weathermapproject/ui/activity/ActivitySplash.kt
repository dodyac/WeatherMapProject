package com.acxdev.weathermapproject.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.acxdev.commonFunction.common.IConstant
import com.acxdev.commonFunction.util.Preference.Companion.getPrefs
import com.acxdev.commonFunction.util.Preference.Companion.isLogged
import com.acxdev.sqlitez.SqliteZAsset.Companion.createDBTableWithId
import com.acxdev.weathermapproject.data.model.User

class ActivitySplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isFirstTime = getPrefs().getString(IConstant.IS_FIRST_TIME, IConstant.EMPTY).toString().isEmpty()
        if (isFirstTime) {
            createDBTableWithId(User::class.java)
        }

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
