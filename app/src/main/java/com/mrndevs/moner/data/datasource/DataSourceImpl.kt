package com.mrndevs.moner.data.datasource

import com.mrndevs.moner.data.model.Date
import com.mrndevs.moner.data.preference.MonerPreference
import com.mrndevs.moner.data.room.dao.MonerDao
import com.mrndevs.moner.data.room.model.AmountEntity
import com.mrndevs.moner.data.room.model.RecordEntity
import com.mrndevs.moner.utils.Constant
import com.mrndevs.moner.utils.SortUtils
import kotlinx.coroutines.flow.Flow

class DataSourceImpl(private val monerPreference: MonerPreference, private val monerDao: MonerDao) :
    DataSource {
    override fun getDate(): Flow<Date> {
        return monerPreference.date
    }

    override suspend fun saveMonthAndYearQuery(month: String, year: String) {
        return monerPreference.saveMonthAndYearQuery(month, year)
    }

    override suspend fun insertRecord(record: RecordEntity): Long {
        return monerDao.insertRecord(record)
    }

    override suspend fun updateRecord(record: RecordEntity): Int {
        return monerDao.updateRecord(record)
    }

    override suspend fun deleteRecord(record: RecordEntity): Int {
        return monerDao.deleteRecord(record)
    }

    override suspend fun getRecords(date: String): List<RecordEntity> {
        return monerDao.getRecords(
            SortUtils.getSortedQuery(
                filter = Constant.GET_RECORDS,
                date = date
            )
        )
    }

    override suspend fun getRecordsByDay(date: String): List<RecordEntity> {
        return monerDao.getRecords(
            SortUtils.getSortedQuery(
                filter = Constant.GET_RECORDS_BY_DAY,
                date = date
            )
        )
    }

    override suspend fun getRecordsDate(date: String): List<RecordEntity> {
        return monerDao.getRecords(
            SortUtils.getSortedQuery(
                filter = Constant.GET_RECORDS_DATE,
                date = date
            )
        )
    }

    override suspend fun getAmountRecordByType(recordType: String, date: String): AmountEntity {
        return monerDao.getAmountRecordByType(recordType, date)
    }

    override suspend fun getRecordById(id: Int): RecordEntity {
        return monerDao.getRecordById(id)
    }

    override suspend fun getRecordCategory(date: String): List<RecordEntity> {
        return monerDao.getRecordCategory(date)
    }
}