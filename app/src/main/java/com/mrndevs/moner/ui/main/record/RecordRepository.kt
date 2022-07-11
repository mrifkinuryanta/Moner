package com.mrndevs.moner.ui.main.record

import com.mrndevs.moner.data.datasource.DataSource
import com.mrndevs.moner.data.model.Date
import com.mrndevs.moner.data.room.model.AmountEntity
import com.mrndevs.moner.data.room.model.RecordEntity
import kotlinx.coroutines.flow.Flow

class RecordRepository(private val dataSource: DataSource) : RecordContract.Repository {
    override fun getDate(): Flow<Date> {
        return dataSource.getDate()
    }

    override suspend fun saveMonthAndYearQuery(month: String, year: String) {
        return dataSource.saveMonthAndYearQuery(month, year)
    }

    override suspend fun getAmountRecordByType(recordType: String, date: String): AmountEntity {
        return dataSource.getAmountRecordByType(recordType, date)
    }

    override suspend fun getRecordCategory(date: String): List<RecordEntity> {
        return dataSource.getRecordCategory(date)
    }
}