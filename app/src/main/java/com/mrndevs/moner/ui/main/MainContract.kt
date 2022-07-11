package com.mrndevs.moner.ui.main

import androidx.lifecycle.LiveData
import com.mrndevs.moner.base.BaseContract
import com.mrndevs.moner.base.model.Resource

interface MainContract {
    interface View : BaseContract.BaseView {
        fun initBottomNav()
        fun setClickListener()
    }

    interface ViewModel {
        fun getResultLiveData(): LiveData<Resource<String>>
        fun saveMonthAndYearQuery(month: String, year: String)
    }

    interface Repository {
        suspend fun saveMonthAndYearQuery(month: String, year: String)
    }
}