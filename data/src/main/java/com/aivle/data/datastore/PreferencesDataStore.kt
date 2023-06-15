package com.aivle.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.aivle.data.entity.AddressEntity
import kotlinx.coroutines.flow.Flow

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "FuniBuni")

interface PreferencesDataStore {

    val address: Flow<AddressEntity?>

    suspend fun setAddress(address: AddressEntity)

    val refreshToken: Flow<String?>

    suspend fun setRefreshToken(token: String)

    val accessToken: Flow<String?>

    suspend fun setAccessToken(token: String)

}