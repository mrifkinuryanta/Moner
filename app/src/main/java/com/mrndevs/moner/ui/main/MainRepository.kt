package com.mrndevs.moner.ui.main

import com.mrndevs.moner.data.datasource.DataSource

class MainRepository(private val dataSource: DataSource) : MainContract.Repository {
    override suspend fun saveMonthAndYearQuery(month: String, year: String) {
        return dataSource.saveMonthAndYearQuery(month, year)
    }
}