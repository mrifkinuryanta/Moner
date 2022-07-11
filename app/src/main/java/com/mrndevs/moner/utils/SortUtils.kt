package com.mrndevs.moner.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object SortUtils {
    fun getSortedQuery(
        filter: String,
        date: String
    ): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM ${Constant.TABLE_NAME} ")
        when (filter) {
            Constant.GET_RECORDS -> simpleQuery.append("WHERE strftime('%Y-%m', date) = '$date' ORDER BY date DESC")
            Constant.GET_RECORDS_BY_DAY -> simpleQuery.append("WHERE strftime('%Y-%m-%d', date) = '$date'")
            Constant.GET_RECORDS_DATE -> simpleQuery.append("WHERE strftime('%Y-%m', date) = '$date' GROUP BY date ORDER BY date DESC")
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}