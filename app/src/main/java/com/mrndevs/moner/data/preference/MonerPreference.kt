package com.mrndevs.moner.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mrndevs.moner.BuildConfig
import com.mrndevs.moner.data.model.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MonerPreference(private val context: Context) {
    val date: Flow<Date>
        get() = context.dataStore.data.map { preferences ->
            Date(preferences[PREF_MONTH_QUERY].orEmpty(), preferences[PREF_YEAR_QUERY].orEmpty())

        }

    suspend fun saveMonthAndYearQuery(month: String, year: String) {
        context.dataStore.edit { preferences ->
            preferences[PREF_MONTH_QUERY] = month
            preferences[PREF_YEAR_QUERY] = year
        }
    }

    companion object {
        private const val PREFERENCES_NAME = BuildConfig.APPLICATION_ID + "_preferences"
        private val PREF_MONTH_QUERY = stringPreferencesKey("pref_month_query")
        private val PREF_YEAR_QUERY = stringPreferencesKey("pref_year_query")

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = PREFERENCES_NAME
        )
    }
}