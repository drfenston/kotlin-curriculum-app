package com.cyrilmaquaire.curriculum.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreData(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storeData")
        val APP_LOGIN = stringPreferencesKey("APP_LOGIN")
        val APP_PASSWORD = stringPreferencesKey("APP_PASSWORD")
        val LOGIN_CHECK_SAVE = stringPreferencesKey("LOGIN_CHECK_SAVE")
    }

    // Récupérer le login
    val login: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[APP_LOGIN]
        }

    // Récupérer le password
    val password: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[APP_PASSWORD]
        }

    fun getData(key: Preferences.Key<String>): Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[key] ?: ""
        }

    suspend fun saveData(key: Preferences.Key<String>, name: String) {
        context.dataStore.edit { preferences ->
            preferences[key] = name
        }
    }

}