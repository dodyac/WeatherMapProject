package com.acxdev.weathermapproject.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.Toast.Companion.toasty
import com.acxdev.commonFunction.util.view.ITextInputLayout.Companion.alertMail
import com.acxdev.sqlitez.SqliteZAsset.Companion.insertDB
import com.acxdev.sqlitez.SqliteZAsset.Companion.rowDBExist
import com.acxdev.weathermapproject.R
import com.acxdev.weathermapproject.common.BaseActivity
import com.acxdev.weathermapproject.databinding.ActivityRegisterBinding
import com.acxdev.weathermapproject.data.model.User
import com.acxdev.weathermapproject.util.Toolbar.setToolbar
import com.acxdev.weathermapproject.util.isNotEmpty
import com.acxdev.weathermapproject.util.toEditString

class ActivitySignUp : BaseActivity<ActivityRegisterBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = ActivityRegisterBinding::inflate


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            toolbar.setToolbar(R.string.registrasi)

            profile.simpan.setOnClickListener {
                if(profile.username.isNotEmpty() && profile.mail.alertMail() && profile.name.isNotEmpty() && profile.password.isNotEmpty()) {
                    val user = User(
                        profile.username.toEditString(),
                        profile.mail.toEditString(),
                        profile.name.toEditString(),
                        profile.password.toEditString()
                    )
                    if(rowDBExist(User::class.java, "username", "'${profile.username.toEditString()}'")) {
                        toasty(Toast.ERROR, "Username ${profile.username.toEditString()} telah digunakan")
                    } else{
                        insertDB(User::class.java, user)
                        toasty(Toast.SUCCESS, getString(R.string.registrasi_sukses))
                        finish()
                    }
                }
            }
        }
    }
}