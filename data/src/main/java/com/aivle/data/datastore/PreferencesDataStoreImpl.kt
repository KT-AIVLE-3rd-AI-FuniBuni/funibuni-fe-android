package com.aivle.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.aivle.data.entity.AddressEntity
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

    companion object {
        private val ADDRESS = stringPreferencesKey("address")
    }
}