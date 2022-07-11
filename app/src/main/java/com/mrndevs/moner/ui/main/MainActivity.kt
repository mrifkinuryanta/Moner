package com.mrndevs.moner.ui.main

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.mrndevs.moner.R
import com.mrndevs.moner.base.BaseActivity
import com.mrndevs.moner.databinding.ActivityMainBinding
import com.mrndevs.moner.ui.recordform.RecordFormActivity
import com.mrndevs.moner.utils.Utils

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),
    MainContract.View {
    private lateinit var viewModel: MainViewModel

    override fun initView() {
        supportActionBar?.hide()
        initBottomNav()
        setClickListener()
    }

    override fun initViewModel() {
        val repository = MainRepository(getDataSource())
        viewModel = MainViewModel(repository)
    }

    override fun initSplashscreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                val month = Utils.getCurrentMonth()
                val year = Utils.getCurrentYear()
                viewModel.saveMonthAndYearQuery(month, year)
                viewModel.isLoading.value
            }
        }
    }

    override fun initBottomNav() {
        getViewBinding().bottomNavigationView.background = null
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment
        NavigationUI.setupWithNavController(
            getViewBinding().bottomNavigationView,
            navHostFragment.navController
        )
    }

    override fun setClickListener() {
        getViewBinding().fab.setOnClickListener {
            RecordFormActivity.startActivity(this)
        }
    }
}