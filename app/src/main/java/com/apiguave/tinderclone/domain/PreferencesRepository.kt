package com.apiguave.tinderclone.domain

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val APP_DATA = "app_data"
val Context.dataStore by preferencesDataStore(name = APP_DATA)
class PreferencesRepository (context: Context){
    private val userPreferencesStore: DataStore<Preferences> = context.dataStore

    private object PreferencesKeys{
        val HAS_CHECKED_PENDING_INFO = booleanPreferencesKey("has_checked_pending_info")
    }

    val hasCheckedPendingInfoFlow: Flow<Boolean> = userPreferencesStore.data.map { preferences ->
        val hasCheckedPendingInfo = preferences[PreferencesKeys.HAS_CHECKED_PENDING_INFO] ?: false
        hasCheckedPendingInfo
    }
}