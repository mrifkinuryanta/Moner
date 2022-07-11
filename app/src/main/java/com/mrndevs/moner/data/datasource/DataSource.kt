package com.mrndevs.moner.data.datasource

import com.mrndevs.moner.data.model.Date
import com.mrndevs.moner.data.room.model.AmountEntity
import com.mrndevs.moner.data.room.model.RecordEntity
import kotlinx.coroutines.flow.Flow

interface DataSource {
    fun getDate(): Flow<Date>
    suspend fun saveMonthAndYearQuery(month: String, year: String)
    suspend fun insertRecord(record: RecordEntity): Long
    suspend fun updateRecord(record: RecordEntity): Int
    suspend fun deleteRecord(record: RecordEntity): Int
    suspend fun getRecords(date: String): List<RecordEntity>
    suspend fun getRecordsByDay(date: String): List<RecordEntity>
    suspend fun getRecordsDate(date: String): List<RecordEntity>
    suspend fun getAmountRecordByType(recordType: String, date: String): AmountEntity
    suspend fun getRecordById(id: Int): RecordEntity
    suspend fun getRecordCategory(date: String): List<RecordEntity>
}