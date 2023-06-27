package com.aivle.data.repository

import com.aivle.data.datastore.PreferencesDataStore
import com.aivle.data.di.api.FuniBuniSignWithTokenApiQualifier
import com.aivle.domain.repository.RefreshTokenRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class RefreshTokenRepositoryImpl @Inject constructor(
    private val dataStore: PreferencesDataStore
) : RefreshTokenRepository {

    override fun getRefreshToken(): String? {
        return runBlocking { dataStore.refreshToken.first() }
    }

    override fun setRefreshToken(token: String) {
        runBlocking { dataStore.setRefreshToken(token) }
    }
}


