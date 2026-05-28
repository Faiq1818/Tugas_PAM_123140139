package com.pertemuan5.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val SORT_BY_TITLE = booleanPreferencesKey("sort_by_title")
    }

    val isDarkMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_DARK_MODE] ?: false
    }

    val sortByTitle: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SORT_BY_TITLE] ?: false
    }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = enabled
        }
    }

    suspend fun setSortByTitle(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[SORT_BY_TITLE] = enabled
        }
    }
}
