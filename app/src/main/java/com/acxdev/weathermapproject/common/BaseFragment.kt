package com.acxdev.weathermapproject.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.IConstant
import com.acxdev.commonFunction.util.IFunction.Companion.useCurrentTheme
import com.acxdev.commonFunction.util.Preference.Companion.getPrefs
import com.acxdev.commonFunction.util.Preference.Companion.isLogged
import com.acxdev.sqlitez.SqliteZAsset.Companion.readDB
import com.acxdev.sqlitez.SqliteZAsset.Companion.readDBDefaultPrimary
import com.acxdev.weathermapproject.data.model.User
import com.google.gson.Gson

abstract class BaseFragment<out VB : ViewBinding> : Fragment() {

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    protected val gone: Int = View.GONE
    protected val visible: Int = View.VISIBLE

    protected lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        requireContext().useCurrentTheme()
        super.onCreate(savedInstanceState)
        if(requireContext().isLogged()) {
            user = requireContext().readDB(User::class.java, requireContext().getPrefs().getLong(Constant.USER_LOGGED, 1L))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater(inflater)
        return binding.root
    }

    protected abstract val bindingInflater: (LayoutInflater) -> ViewBinding

    fun instance(data: String? = null): String = arguments?.getString(data ?: IConstant.DATA)!!

    fun <T> instanceAs(cls: Class<T>, data: String? = null): T =
        Gson().fromJson(requireArguments().getString(data ?: IConstant.DATA), cls)
}