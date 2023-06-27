package com.aivle.data.repository

import com.aivle.data.datastore.PreferencesDataStore
import com.aivle.data.di.api.FuniBuniApiQualifier
import com.aivle.domain.repository.AccessTokenRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AccessTokenRepositoryImpl @Inject constructor(
    private val dataStore: PreferencesDataStore
) : AccessTokenRepository {

    override fun getAccessToken(): String? {
        return runBlocking { dataStore.accessToken.first() }
    }

    override fun setAccessToken(token: String) {
        runBlocking { dataStore.setAccessToken(token) }
    }
}

