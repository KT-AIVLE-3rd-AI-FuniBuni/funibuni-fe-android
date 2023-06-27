package com.aivle.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "FuniBuni")

interface PreferencesDatastore {

    val refreshToken: Flow<String?>

    suspend fun setRefreshToken(token: String)

    val accessToken: Flow<String?>

    suspend fun setAccessToken(token: String)

}