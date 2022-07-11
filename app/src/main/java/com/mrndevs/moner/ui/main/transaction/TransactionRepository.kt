package com.mrndevs.moner.ui.main.transaction

import com.mrndevs.moner.data.datasource.DataSource
import com.mrndevs.moner.data.model.Date
import com.mrndevs.moner.data.room.model.RecordEntity
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val dataSource: DataSource) : TransactionContract.Repository {
    override fun getDate(): Flow<Date> {
        return dataSource.getDate()
    }

    override suspend fun saveMonthAndYearQuery(month: String, year: String) {
        return dataSource.saveMonthAndYearQuery(month, year)
    }

    override suspend fun getRecords(date: String): List<RecordEntity> {
        return dataSource.getRecords(date)
    }

    override suspend fun getRecordsByDay(date: String): List<RecordEntity> {
        return dataSource.getRecordsByDay(date)
    }

    override suspend fun getRecordsDate(date: String): List<RecordEntity> {
        return dataSource.getRecordsDate(date)
    }
}