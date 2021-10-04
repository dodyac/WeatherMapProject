package com.acxdev.weathermapproject.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.Preference.Companion.getPrefs
import com.acxdev.commonFunction.util.Preference.Companion.logout
import com.acxdev.commonFunction.util.Toast.Companion.toasty
import com.acxdev.commonFunction.util.view.ITextInputLayout.Companion.alertMail
import com.acxdev.sqlitez.SqliteZAsset.Companion.updateDBDefaultPrimary
import com.acxdev.weathermapproject.R
import com.acxdev.weathermapproject.common.BaseFragment
import com.acxdev.weathermapproject.common.Constant
import com.acxdev.weathermapproject.data.model.User
import com.acxdev.weathermapproject.databinding.FragmentProfileBinding
import com.acxdev.weathermapproject.ui.activity.ActivitySignIn
import com.acxdev.weathermapproject.util.isNotEmpty
import com.acxdev.weathermapproject.util.setText
import com.acxdev.weathermapproject.util.toEditString
import dev.shreyaspatil.MaterialDialog.MaterialDialog

class FragmentProfile : BaseFragment<FragmentProfileBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentProfileBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            profile.username.setText(user.username)
            profile.mail.setText(user.email)
            profile.name.setText(user.name)
            profile.password.setText(user.pin)

            profile.simpan.text = getString(R.string.perbarui)
            profile.simpan.setOnClickListener {
                if(profile.username.isNotEmpty() && profile.mail.alertMail() && profile.name.isNotEmpty() && profile.password.isNotEmpty()) {
                    val user = User(
                        profile.username.toEditString(),
                        profile.mail.toEditString(),
                        profile.name.toEditString(),
                        profile.password.toEditString()
                    )
                    try {
                        requireContext().updateDBDefaultPrimary(User::class.java, user,
                            requireContext().getPrefs().getLong(Constant.USER_LOGGED, 1))
                        toasty(Toast.SUCCESS, R.string.profil_updated)
                    } catch (e: Exception) {
                        e.localizedMessage?.let { toasty(Toast.ERROR, it) }
                    }
                }
            }

            profile.keluar.visibility = visible
            profile.keluar.setOnClickListener {
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