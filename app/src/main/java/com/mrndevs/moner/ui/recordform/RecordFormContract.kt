package com.mrndevs.moner.ui.recordform

import androidx.lifecycle.LiveData
import com.mrndevs.moner.base.BaseContract
import com.mrndevs.moner.base.model.Resource
import com.mrndevs.moner.data.room.model.RecordEntity

class RecordFormContract {
    interface View : BaseContract.BaseView {
        fun setClickListener()
        fun initEditTextPrice()
    }

    interface ViewModel {
        fun getResultLiveData(): LiveData<Resource<Pair<Int, RecordEntity>>>
        fun getRecordById(id: Int)
        fun insertRecord(record: RecordEntity)
        fun updateRecord(record: RecordEntity)
    }

    interface Repository {
        suspend fun getRecordById(id: Int): RecordEntity
        suspend fun insertRecord(record: RecordEntity): Long
        suspend fun updateRecord(record: RecordEntity): Int
    }
}