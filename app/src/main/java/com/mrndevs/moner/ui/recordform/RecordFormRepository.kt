package com.mrndevs.moner.ui.recordform

import com.mrndevs.moner.data.datasource.DataSource
import com.mrndevs.moner.data.room.model.RecordEntity

class RecordFormRepository(private val dataSource: DataSource) : RecordFormContract.Repository {
    override suspend fun getRecordById(id: Int): RecordEntity {
        return dataSource.getRecordById(id)
    }

    override suspend fun insertRecord(record: RecordEntity): Long {
        return dataSource.insertRecord(record)
    }

    override suspend fun updateRecord(record: RecordEntity): Int {
        return dataSource.updateRecord(record)
    }
}