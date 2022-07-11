package com.mrndevs.moner.ui.detail

import com.mrndevs.moner.data.datasource.DataSource
import com.mrndevs.moner.data.room.model.RecordEntity

class DetailRepository(private val dataSource: DataSource) : DetailContract.Repository {
    override suspend fun getRecordById(id: Int): RecordEntity {
        return dataSource.getRecordById(id)
    }

    override suspend fun deleteRecord(record: RecordEntity): Int {
        return dataSource.deleteRecord(record)
    }
}