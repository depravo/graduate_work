package com.depravo.thesugarshop.util

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class DataStoreManager(val context: Context) {

    suspend fun saveUserId(user_id: Int) {
        context.dataStore.edit { pref ->
            pref[intPreferencesKey("user_id")] = user_id
        }
    }

    fun getUserId(): Flow<Int> = context.dataStore.data.map { pref ->
        Log.d("MYTAG", pref[intPreferencesKey("user_id")].toString())
        pref[intPreferencesKey("user_id")] ?: 0
    }
}