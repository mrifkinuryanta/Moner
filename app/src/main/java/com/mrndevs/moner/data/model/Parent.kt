package com.mrndevs.moner.data.model

import com.mrndevs.moner.data.room.model.RecordEntity

data class Parent(
    var parentItemTitle: String,
    var childItemList: List<RecordEntity>
)
