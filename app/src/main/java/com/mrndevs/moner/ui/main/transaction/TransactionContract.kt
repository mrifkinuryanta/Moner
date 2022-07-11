package com.mrndevs.moner.ui.main.transaction

import androidx.lifecycle.LiveData
import com.mrndevs.moner.base.BaseContract
import com.mrndevs.moner.base.model.Resource
import com.mrndevs.moner.data.model.Date
import com.mrndevs.moner.data.model.Parent
import com.mrndevs.moner.data.room.model.RecordEntity
import kotlinx.coroutines.flow.Flow

interface TransactionContract {
    interface View : BaseContract.BaseView {
        fun initAdapter()
        fun setClickListener()
        fun saveMonthAndYear()
        fun getMonthAndYear()
        fun getRecords()
    }

    interface ViewModel {
        fun getDateLiveData(): LiveData<Date>
        fun getRecordsLiveData(): LiveData<Resource<List<Parent>>>
        fun getMonthAndYear()
        fun saveMonthAndYearQuery(month: String, year: String)
        fun getRecords(date: String)
    }

    interface Repository {
        fun getDate(): Flow<Date>
        suspend fun saveMonthAndYearQuery(month: String, year: String)
        suspend fun getRecords(date: String): List<RecordEntity>
        suspend fun getRecordsByDay(date: String): List<RecordEntity>
        suspend fun getRecordsDate(date: String): List<RecordEntity>
    }
}