package com.aivle.data.repository

import com.aivle.data.datastore.PreferencesDatastore
import com.aivle.domain.repository.AccessTokenRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AccessTokenRepositoryImpl @Inject constructor(
    private val dataStore: PreferencesDatastore
) : AccessTokenRepository {

    override fun getAccessToken(): String? {
        return runBlocking { dataStore.accessToken.first() }
    }

    override fun setAccessToken(token: String) {
        runBlocking { dataStore.setAccessToken(token) }
    }
}