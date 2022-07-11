package com.mrndevs.moner.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.mrndevs.moner.data.room.model.RecordEntity
import java.text.DateFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun showToastLong(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun hideSoftKeyboard(activity: Activity, view: View) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getTime(): Long {
        return System.currentTimeMillis()
    }

    fun getDate(date: Long, pattern: String): String {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        if (date > 0) {
            calendar.time = Date(date)
        }
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(calendar.time)
    }

    fun getUpdateDate(): String {
        return getDate(getTime(), Constant.DATE_PATTERN_DB)
    }

    fun getCurrentMonth(): String {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        return getMonths()[calendar.get(Calendar.MONTH)]
    }

    fun getCurrentYear(): String {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        return calendar.get(Calendar.YEAR).toString()
    }

    fun getMonths(): Array<String> {
        val dateFormatSymbols = DateFormatSymbols()
        return dateFormatSymbols.months
    }

    fun getYears(): Array<String> {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        val years = mutableListOf<String>()
        for (year in calendar.get(Calendar.YEAR).minus(10)..calendar.get(Calendar.YEAR)) {
            years.add(year.toString())
        }
        return years.toTypedArray()
    }

    fun getMonthNumber(monthName: String): String {
        val month = monthName.ifEmpty { getCurrentMonth() }
        val date = SimpleDateFormat("MMMM", Locale.getDefault()).parse(month)
        val calendar = Calendar.getInstance(TimeZone.getDefault()).apply {
            time = date as Date
        }
        val result = calendar.get(Calendar.MONTH) + 1
        return if (result >= 10) result.toString() else "0$result"
    }

    fun dateFormatter(date: String, beforePattern: String, afterPattern: String): String {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        SimpleDateFormat(beforePattern, Locale.getDefault()).parse(date).apply {
            calendar.time = this as Date
        }
        val formatter = SimpleDateFormat(afterPattern, Locale.getDefault())
        return formatter.format(calendar.time)
    }

    fun currencyFormatter(stringText: String): String {
        val locale = Locale("in", "ID")
        val cleanString = stringText.replace("[ Rp,./-]".toRegex(), "")
        val parsed = if (cleanString.isEmpty()) 0.0 else cleanString.toDouble()
        return NumberFormat.getCurrencyInstance(locale).format(parsed).replace(",00", "")
    }

    fun currencyToInt(stringText: String): Int {
        val cleanString = stringText.replace("[ Rp,./-]".toRegex(), "")
        return if (cleanString.isEmpty()) 0 else cleanString.toInt()
    }

    fun dpToPixels(context: Context?, dp: Int): Int {
        if (context != null) {
            val scale = context.resources.displayMetrics.density
            return (dp * scale + 0.5f).toInt()
        }
        return 0
    }

    fun recordEmpty(): RecordEntity {
        return RecordEntity(
            Constant.ZERO,
            Constant.ZERO,
            Constant.STRING_EMPTY,
            Constant.STRING_EMPTY,
            Constant.STRING_EMPTY,
            Constant.STRING_EMPTY,
            Constant.STRING_EMPTY,
            Constant.STRING_EMPTY,
            Constant.STRING_EMPTY
        )
    }
}