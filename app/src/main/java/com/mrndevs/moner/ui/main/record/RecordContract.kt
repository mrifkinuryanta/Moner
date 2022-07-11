package com.mrndevs.moner.ui.main.record

import androidx.lifecycle.LiveData
import com.mrndevs.moner.base.BaseContract
import com.mrndevs.moner.base.model.Resource
import com.mrndevs.moner.data.model.AmountMoney
import com.mrndevs.moner.data.model.Date
import com.mrndevs.moner.data.room.model.AmountEntity
import com.mrndevs.moner.data.room.model.RecordEntity
import kotlinx.coroutines.flow.Flow

interface RecordContract {
    interface View : BaseContract.BaseView {
        fun initAdapter()
        fun setClickListener()
        fun saveMonthAndYear()
        fun getMonthAndYear()
        fun getRecords()
    }

    interface ViewModel {
        fun getDateLiveData(): LiveData<Date>
        fun getAmountMoneyLiveData(): LiveData<Resource<AmountMoney>>
        fun getRecordCategoryLiveData(): LiveData<Resource<List<RecordEntity>>>
        fun getMonthAndYear()
        fun saveMonthAndYearQuery(month: String, year: String)
        fun getAmountMoney(date: String)
        fun getRecordCategory(date: String)
    }

    interface Repository {
        fun getDate(): Flow<Date>
        suspend fun saveMonthAndYearQuery(month: String, year: String)
        suspend fun getAmountRecordByType(recordType: String, date: String): AmountEntity
        suspend fun getRecordCategory(date: String): List<RecordEntity>
    }
}