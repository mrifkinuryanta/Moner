package com.mrndevs.moner.data.room.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record")
data class RecordEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "amount")
    var amount: Int,

    @ColumnInfo(name = "payment")
    var payment: String? = null,

    @ColumnInfo(name = "category")
    var category: String,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "note")
    var note: String? = null,

    @ColumnInfo(name = "record_type")
    var recordType: String,

    @ColumnInfo(name = "create_at")
    val createAt: String? = null,

    @ColumnInfo(name = "update_at")
    val updateAt: String
)
