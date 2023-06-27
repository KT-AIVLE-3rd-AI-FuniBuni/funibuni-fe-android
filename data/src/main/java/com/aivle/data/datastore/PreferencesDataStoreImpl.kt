package com.aivle.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.aivle.data.entity.address.AddressEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesDataStore {

    override val address: Flow<AddressEntity?> = context.dataStore.data.map { preferences ->
        preferences[ADDRESS]?.let { AddressEntity(it) }
    }

    override suspend fun setAddress(address: AddressEntity) {
        context.dataStore.edit { preferences ->
            preferences[ADDRESS] = address.value
        }
    }

    override val refreshToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN]
    }

    override suspend fun setRefreshToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = token
        }
    }

    override val accessToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN]
    }

    override suspend fun setAccessToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN]
        }
    }

    companion object {
        private val ADDRESS = stringPreferencesKey("address")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }
}


