package com.aivle.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDatastoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesDatastore {

    override val refreshToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN]
    }

    override suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = token
        }
    }

    override val accessToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN]
    }

    override suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN]
        }
    }

    override suspend fun saveAuthTokens(refresh: String, access: String) {
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = refresh
            preferences[ACCESS_TOKEN] = access
        }
    }

    override suspend fun deleteAuthTokens() {
        context.dataStore.edit { preferences ->
            preferences.remove(REFRESH_TOKEN)
            preferences.remove(ACCESS_TOKEN)
        }
    }

    companion object {
        private const val TAG = "PreferencesDatastoreImpl"

        private val ADDRESS = stringPreferencesKey("address")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }
}