package com.acxdev.weathermapproject.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.Preference.Companion.logged
import com.acxdev.commonFunction.util.Preference.Companion.putPrefs
import com.acxdev.commonFunction.util.Toast.Companion.toasty
import com.acxdev.weathermapproject.R
import com.acxdev.weathermapproject.common.BaseActivity
import com.acxdev.weathermapproject.common.Constant
import com.acxdev.weathermapproject.databinding.ActivitySignInBinding
import com.acxdev.weathermapproject.util.isNotEmpty
import com.acxdev.weathermapproject.util.setText
import com.acxdev.weathermapproject.util.toEditString
import kotlinx.coroutines.launch

class ActivitySignIn : BaseActivity<ActivitySignInBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = ActivitySignInBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {

            signIn.setOnClickListener {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    val username = username.toEditString()
                    lifecycleScope.launch {
                        if (dao.isUsernameExist(username)) {
                            val user = dao.getUser(username)
                            if (user.pin == password.toEditString()) {
                                Intent(this@ActivitySignIn, MainActivity::class.java).also {
                                    logged()
                                    putPrefs(Constant.USER_LOGGED, user.username)
                                    startActivity(it)
                                    finish()
                                }
                            } else {
                                toasty(Toast.ERROR, R.string.wrong_pin)
                            }
                        } else {
                            toasty(Toast.ERROR, R.string.username_not_found)
                        }
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
                    val username = username.toEditString()
                    lifecycleScope.launch {
                        if (dao.isUsernameExist(username)) {
                            val user = dao.getUser(username)
                            password.setText(user.pin)
                        } else {
                            toasty(Toast.ERROR, R.string.username_not_found)
                        }
                    }
                }
            }
        }
    }
}