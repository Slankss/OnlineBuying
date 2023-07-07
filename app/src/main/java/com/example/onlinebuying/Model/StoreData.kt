package com.example.onlinebuying.Model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.compose.runtime.*
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class StoreData(private val context : Context) {


    companion object {
        private val Context.dataStore : DataStore<Preferences>
            by preferencesDataStore("storeData")
        val FIRST_TIME = booleanPreferencesKey("store_data")

    }

    val getData : Flow<Boolean?> = context.dataStore.data
        .map{ preferences ->
            preferences[FIRST_TIME] ?: false
        }

    suspend fun saveData(first_time : Boolean){
        context.dataStore.edit{ preferences ->
            preferences[FIRST_TIME] = first_time
        }
    }

}

