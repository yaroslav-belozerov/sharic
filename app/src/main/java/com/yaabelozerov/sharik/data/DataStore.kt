package com.yaabelozerov.sharik.data

import android.content.Context
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.map

class DataStore(private val context: Context) {
    private val Context.dataStore by preferencesDataStore("preferences")
    private val dataStore = context.dataStore

    private val tokenKey = stringPreferencesKey("token")

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[tokenKey] = token
        }
    }
    fun getToken() = dataStore.data.map { it[tokenKey] ?: "" }
}