package com.acv.blockchain.services

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.acv.blockchain.utils.ICoroutineSupport
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

sealed class DataKeys<T>(val value: Preferences.Key<T>) {
    data class Token(val key: Preferences.Key<String> = stringPreferencesKey("token")) : DataKeys<String>(key)
    data class ServerTime(val key: Preferences.Key<String> = stringPreferencesKey("serverTime")) : DataKeys<String>(key)
    data class Expiration(val key: Preferences.Key<String> = stringPreferencesKey("expiration")) : DataKeys<String>(key)
}

@ActivityScoped
class DatastoreService @Inject constructor(@ActivityContext val context: Context): ICoroutineSupport {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    fun <T> readFlow(key: DataKeys<T>, defaultValue: T): Flow<T> {
        return context.dataStore.data
            .map { preferences ->
                preferences[key.value] ?: defaultValue
            }
    }

    fun <T> save(value: T, key: DataKeys<T>) {
        launch {
            context.dataStore.edit { settings ->
                settings[key.value] = value
            }
        }
    }

    /**
     * Очистка данных
     */
    fun clear() {
        launch {
            context.dataStore.edit {
                it.clear()
            }
        }
    }
}