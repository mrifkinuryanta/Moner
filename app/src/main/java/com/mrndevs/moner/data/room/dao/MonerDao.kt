package com.mrndevs.moner.data.room.dao

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.mrndevs.moner.data.room.model.AmountEntity
import com.mrndevs.moner.data.room.model.RecordEntity

@Dao
interface MonerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: RecordEntity): Long

    @Update
    suspend fun updateRecord(record: RecordEntity): Int

    @Delete
    suspend fun deleteRecord(record: RecordEntity): Int

    @RawQuery(observedEntities = [RecordEntity::class])
    suspend fun getRecords(query: SimpleSQLiteQuery): List<RecordEntity>

    @Query("SELECT SUM(amount) as amount, record_type FROM record WHERE strftime('%Y-%m', date) = :date")
    suspend fun getAmountRecords(date: String): AmountEntity

    @Query("SELECT SUM(amount) as amount, record_type FROM record WHERE record_type = :recordType AND strftime('%Y-%m', date) = :date")
    suspend fun getAmountRecordByType(recordType: String, date: String): AmountEntity

    @Query("SELECT * FROM record WHERE id = :id")
    suspend fun getRecordById(id: Int): RecordEntity

    @Query("SELECT id, SUM(amount) as amount, payment, category, date, note, record_type, create_at, update_at FROM record WHERE strftime('%Y-%m', date) = :date GROUP BY category ORDER BY date DESC")
    suspend fun getRecordCategory(date: String): List<RecordEntity>
}