package com.mrndevs.moner.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mrndevs.moner.data.room.dao.MonerDao
import com.mrndevs.moner.data.room.model.RecordEntity

@Database(
    entities = [RecordEntity::class],
    version = 1
)
abstract class MonerDatabase : RoomDatabase() {
    abstract fun monerDao(): MonerDao

    companion object {
        private const val DB_NAME = "Moner.db"

        @Volatile
        private var INSTANCE: MonerDatabase? = null
        fun getInstance(context: Context): MonerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MonerDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}