package com.mrndevs.moner.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.mrndevs.moner.data.datasource.DataSource
import com.mrndevs.moner.data.datasource.DataSourceImpl
import com.mrndevs.moner.data.preference.MonerPreference
import com.mrndevs.moner.data.room.MonerDatabase
import com.mrndevs.moner.utils.Utils

abstract class BaseActivity<B : ViewBinding>(
    val bindingFactory: (LayoutInflater) -> B
) : AppCompatActivity(), BaseContract.BaseView {
    private lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSplashscreen()
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        initView()
        observeViewModel()
    }

    fun getViewBinding(): B = binding

    fun getDataSource(): DataSource {
        val preference = MonerPreference(this)
        val database = MonerDatabase.getInstance(this)
        return DataSourceImpl(preference, database.monerDao())
    }

    fun hideSoftKeyboard() {
        Utils.hideSoftKeyboard(this, binding.root)
    }

    fun showToast(msg: String?) {
        Utils.showToastLong(this, msg.orEmpty())
    }

    abstract fun initView()
    override fun initSplashscreen() {}
    override fun initViewModel() {}
    override fun observeViewModel() {}
}