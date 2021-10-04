package com.acxdev.weathermapproject.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.Preference.Companion.getPrefs
import com.acxdev.commonFunction.util.Preference.Companion.logout
import com.acxdev.commonFunction.util.Toast.Companion.toasty
import com.acxdev.commonFunction.util.view.ITextInputLayout.Companion.alertMail
import com.acxdev.weathermapproject.R
import com.acxdev.weathermapproject.common.BaseFragment
import com.acxdev.weathermapproject.common.Constant
import com.acxdev.weathermapproject.data.model.User
import com.acxdev.weathermapproject.databinding.FragmentProfileBinding
import com.acxdev.weathermapproject.ui.activity.ActivityResetPin
import com.acxdev.weathermapproject.ui.activity.ActivitySignIn
import com.acxdev.weathermapproject.util.isNotEmpty
import com.acxdev.weathermapproject.util.setText
import com.acxdev.weathermapproject.util.toEditString
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentProfile : BaseFragment<FragmentProfileBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentProfileBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            profile.username.isEnabled = false

            lifecycleScope.launch {
                val username = requireContext().getPrefs().getString(Constant.USER_LOGGED,"")!!
                val user = dao.getUser(username)
                profile.username.setText(user.username)
                profile.mail.setText(user.email)
                profile.name.setText(user.name)
                profile.password.setText(user.pin)

                simpan.setOnClickListener {
                    if(profile.username.isNotEmpty() && profile.mail.alertMail() && profile.name.isNotEmpty() && profile.password.isNotEmpty()) {
                        val userNew = User(
                            user.id, profile.username.toEditString(),
                            profile.mail.toEditString(),
                            profile.name.toEditString(),
                            user.pin
                        )

                        lifecycleScope.launch {
                            when {
                                user.username == profile.username.toEditString() -> {
                                    dao.updateUser(userNew)
                                    toasty(
                                        Toast.SUCCESS,
                                        R.string.profil_updated
                                    )
                                }
                                dao.isUsernameExist(profile.username.toEditString()) -> {
                                    toasty(
                                        Toast.ERROR,
                                        getString(
                                            R.string.username_already_used,
                                            profile.username.toEditString()
                                        )
                                    )
                                }
                                else -> {
                                    dao.updateUser(userNew)
                                    toasty(
                                        Toast.SUCCESS,
                                        R.string.profil_updated
                                    )
                                }
                            }
                        }
                    }
                }

                gantiPin.setOnClickListener {
                    Intent(requireContext(), ActivityResetPin::class.java)
                        .putExtra(Constant.USER_LOGGED, Gson().toJson(user))
                        .also {
                            startActivity(it)
                        }
                }
            }

            profile.password.visibility = gone

            keluar.setOnClickListener {
                MaterialDialog.Builder(requireActivity())
                    .setAnimation(R.raw.sign_out)
                    .setMessage(getString(R.string.suretoexit))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.exit)) { dialogInterface, _ ->
                        requireContext().logout()
                        Intent(requireContext(), ActivitySignIn::class.java).also {
                            startActivity(it)
                        }
                        requireActivity().finish()
                        dialogInterface.dismiss()
                    }
                    .setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ -> dialogInterface.dismiss() }
                    .build().show()
            }
        }

    }
}