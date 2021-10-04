package com.acxdev.weathermapproject.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.Toast.Companion.toasty
import com.acxdev.weathermapproject.R
import com.acxdev.weathermapproject.common.BaseActivity
import com.acxdev.weathermapproject.common.Constant
import com.acxdev.weathermapproject.data.model.User
import com.acxdev.weathermapproject.databinding.ActivityResetPinBinding
import com.acxdev.weathermapproject.util.Toolbar.setToolbar
import com.acxdev.weathermapproject.util.isEqual
import com.acxdev.weathermapproject.util.isNotEmpty
import com.acxdev.weathermapproject.util.toEditString
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ActivityResetPin : BaseActivity<ActivityResetPinBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = ActivityResetPinBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            toolbar.setToolbar(R.string.reset_pin)

            val user = Gson().fromJson(intent.getStringExtra(Constant.USER_LOGGED), User::class.java)

            gantiPin.setOnClickListener {
                if(currentPassword.isNotEmpty() && password.isEqual(verifyPassword)) {
                    if(currentPassword.toEditString() != user.pin) {
                        toasty(Toast.ERROR, R.string.wrong_pin)
                    } else {
                        lifecycleScope.launch {
                            val userNew = User(
                                user.id,
                                user.username,
                                user.email,
                                user.name,
                                verifyPassword.toEditString()
                            )
                            dao.updateUser(userNew)
                            user.pin = verifyPassword.toEditString()
                            toasty(Toast.SUCCESS, R.string.pin_updated)
                        }
                    }
                }
            }
        }
    }
}