package com.mrndevs.moner.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.mrndevs.moner.data.datasource.DataSource
import com.mrndevs.moner.data.datasource.DataSourceImpl
import com.mrndevs.moner.data.preference.MonerPreference
import com.mrndevs.moner.data.room.MonerDatabase
import com.mrndevs.moner.utils.Utils

abstract class BaseFragment<B : ViewBinding>(
    val bindingFactory: (LayoutInflater, ViewGroup?, Boolean) -> B
) : Fragment(), BaseContract.BaseView {
    private lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = bindingFactory(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
        observeViewModel()
    }

    fun getViewBinding(): B = binding

    fun getDataSource(): DataSource {
        val preference = MonerPreference(requireContext())
        val database = MonerDatabase.getInstance(requireContext())
        return DataSourceImpl(preference, database.monerDao())
    }

    fun hideSoftKeyboard() {
        Utils.hideSoftKeyboard(requireActivity(), binding.root)
    }

    fun showToast(msg: String?) {
        Utils.showToastLong(requireContext(), msg.orEmpty())
    }

    abstract fun initView()
    override fun initSplashscreen() {}
    override fun initViewModel() {}
    override fun observeViewModel() {}
}