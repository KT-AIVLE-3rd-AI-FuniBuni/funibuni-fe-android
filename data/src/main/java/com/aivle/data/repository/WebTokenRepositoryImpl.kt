package com.aivle.data.repository

import com.aivle.data.datastore.PreferencesDataStore
import com.aivle.domain.repository.WebTokenRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class WebTokenRepositoryImpl @Inject constructor(
    private val dataStore: PreferencesDataStore
) : WebTokenRepository {

    override fun getRefreshToken(): String? {
        return runBlocking { dataStore.refreshToken.first() }
    }

    override fun setRefreshToken(token: String) {
        runBlocking { dataStore.setRefreshToken(token) }
    }

    override fun getAccessToken(): String? {
        return runBlocking { dataStore.accessToken.first() }
    }

    override fun setAccessToken(token: String) {
        runBlocking { dataStore.setAccessToken(token) }
    }
}