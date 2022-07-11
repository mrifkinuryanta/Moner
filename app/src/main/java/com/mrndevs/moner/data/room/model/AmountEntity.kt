package com.mrndevs.moner.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "record")
data class AmountEntity(
    @ColumnInfo(name = "amount")
    var amount: Int,

    @ColumnInfo(name = "date")
    var date: String? = null,

    @ColumnInfo(name = "record_type")
    var recordType: String? = null,
)
