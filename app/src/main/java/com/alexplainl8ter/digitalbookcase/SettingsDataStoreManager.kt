// SettingsDataStoreManager.kt
package com.alexplainl8ter.digitalbookcase

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStoreManager(context: Context) {

    private val apiTypeKey = stringPreferencesKey("api_type")
    private val dataStore = context.settingsDataStore

    // Function to save the selected API type to DataStore
    suspend fun saveApiType(apiType: ApiType) {
        dataStore.edit { preferences ->
            preferences[apiTypeKey] = apiType.name // Save API type name as String
        }
    }

    // Function to get the selected API type from DataStore as a Flow
    val apiTypeFlow: Flow<ApiType> = dataStore.data
        .map { preferences ->
            val apiTypeName = preferences[apiTypeKey] ?: ApiType.GOOGLE_BOOKS.name // Default to Google Books if not set
            ApiType.valueOf(apiTypeName) // Convert String back to ApiType enum
        }
}