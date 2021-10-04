package com.acxdev.weathermapproject.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.Toast.Companion.toasty
import com.acxdev.commonFunction.util.view.ITextInputLayout.Companion.alertMail
import com.acxdev.weathermapproject.R
import com.acxdev.weathermapproject.common.BaseActivity
import com.acxdev.weathermapproject.databinding.ActivityRegisterBinding
import com.acxdev.weathermapproject.data.model.User
import com.acxdev.weathermapproject.util.Toolbar.setToolbar
import com.acxdev.weathermapproject.util.isEqual
import com.acxdev.weathermapproject.util.isNotEmpty
import com.acxdev.weathermapproject.util.toEditString
import kotlinx.coroutines.launch

class ActivitySignUp : BaseActivity<ActivityRegisterBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = ActivityRegisterBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            toolbar.setToolbar(R.string.registrasi)

            simpan.setOnClickListener {
                if (profile.username.isNotEmpty() && profile.mail.alertMail() && profile.name.isNotEmpty() && profile.password.isEqual(verifyPassword)) {
                    val user = User(
                        0,
                        profile.username.toEditString(),
                        profile.mail.toEditString(),
                        profile.name.toEditString(),
                        profile.password.toEditString()
                    )
                    lifecycleScope.launch {
                        if (dao.isUsernameExist(profile.username.toEditString())) {
                            toasty(
                                Toast.ERROR,
                                getString(
                                        R.string.username_already_used,
                                        profile.username.toEditString()
                                    )
                            )
                        } else {
                            dao.insertUser(user)
                            toasty(
                                Toast.SUCCESS,
                                getString(R.string.registrasi_sukses)
                            )
                            finish()
                        }
                    }
                }
            }
        }
    }
}