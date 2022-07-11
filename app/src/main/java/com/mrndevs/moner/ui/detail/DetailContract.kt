package com.mrndevs.moner.ui.detail

import androidx.lifecycle.LiveData
import com.mrndevs.moner.base.BaseContract
import com.mrndevs.moner.base.model.Resource
import com.mrndevs.moner.data.room.model.RecordEntity

interface DetailContract {
    interface View : BaseContract.BaseView {
        fun setClickListener()
        fun getRecordById(id: Int)
        fun fetchData(record: RecordEntity)
    }

    interface ViewModel {
        fun getRecordLiveData(): LiveData<Resource<Pair<Int, RecordEntity>>>
        fun getRecordById(id: Int)
        fun deleteRecord(record: RecordEntity)
    }

    interface Repository {
        suspend fun getRecordById(id: Int): RecordEntity
        suspend fun deleteRecord(record: RecordEntity): Int
    }
}