package com.aivle.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "FuniBuni")

interface PreferencesDatastore {

    val refreshToken: Flow<String?>

    suspend fun saveRefreshToken(token: String)

    val accessToken: Flow<String?>

    suspend fun saveAccessToken(token: String)

    suspend fun saveAuthTokens(refresh: String, access: String)

    suspend fun deleteAuthTokens()
}