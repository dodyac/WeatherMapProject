package com.acxdev.weathermapproject.ui.activity

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.IConstant
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.Preference.Companion.logged
import com.acxdev.commonFunction.util.Preference.Companion.putPrefs
import com.acxdev.commonFunction.util.Toast.Companion.toasty
import com.acxdev.commonFunction.util.view.ITextInputLayout.Companion.alertMail
import com.acxdev.sqlitez.SqliteZAsset
import com.acxdev.weathermapproject.R
import com.acxdev.weathermapproject.common.BaseActivity
import com.acxdev.weathermapproject.common.Constant
import com.acxdev.weathermapproject.data.model.User
import com.acxdev.weathermapproject.databinding.ActivitySignInBinding
import com.acxdev.weathermapproject.util.isNotEmpty
import com.acxdev.weathermapproject.util.setText
import com.acxdev.weathermapproject.util.toEditString
import com.google.gson.Gson

class ActivitySignIn : BaseActivity<ActivitySignInBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = ActivitySignInBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {

            signIn.setOnClickListener {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    val db = SqliteZAsset(this@ActivitySignIn, User::class.java).readableDatabase
                    val query = "SELECT * FROM User WHERE username = '${username.toEditString()}'"
                    val cursor = db.rawQuery(query, null)
                    if (cursor.count < 1) {
                        cursor.close()
                        db.close()
                        toasty(Toast.ERROR, R.string.username_not_found)
                    } else {
                        cursor.moveToFirst()
                        if(cursor.getString(4).equals(password.toEditString())){
                            Intent(this@ActivitySignIn, MainActivity::class.java).also {
                                logged()
                                putPrefs(Constant.USER_LOGGED, cursor.getLong(0))
                                startActivity(it)
                                finish()
                            }
                        } else{
                            toasty(Toast.ERROR, R.string.wrong_pin)
                        }
                        cursor.close()
                        db.close()
                    }
                }
            }

            signUp.setOnClickListener {
                Intent(this@ActivitySignIn, ActivitySignUp::class.java).also {
                    startActivity(it)
                }
            }

            forgotPassword.setOnClickListener {
                password.isErrorEnabled = false
                if (username.isNotEmpty()) {
                    val db = SqliteZAsset(this@ActivitySignIn, User::class.java).readableDatabase
                    val query = "SELECT * FROM User WHERE username = '${username.toEditString()}'"
                    val cursor = db.rawQuery(query, null)
                    if (cursor.count < 1) {
                        cursor.close()
                        db.close()
                        toasty(Toast.ERROR, R.string.username_not_found)
                    } else {
                        cursor.moveToFirst()
                        password.setText(cursor.getString(4))
                        cursor.close()
                        db.close()
                    }
                }
            }
        }
        putPrefs(IConstant.IS_FIRST_TIME, "no")
    }

}