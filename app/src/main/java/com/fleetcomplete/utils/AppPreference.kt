package com.fleetcomplete.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.first

class AppPreference(context: Context) {
    /*
    DATA STORAGE Class (API KEY Is stored using DATASTORE.
     */
    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = "api_key"
    )

    suspend fun save(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    suspend fun read(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }


    companion object {
        private val KEY = preferencesKey<String>("API_KEY")
    }

}