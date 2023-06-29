package com.aivle.data.repository

import com.aivle.data.datastore.PreferencesDatastore
import com.aivle.domain.repository.RefreshTokenRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class RefreshTokenRepositoryImpl @Inject constructor(
    private val dataStore: PreferencesDatastore
) : RefreshTokenRepository {

    override fun getRefreshToken(): String? {
        return runBlocking { dataStore.refreshToken.first() }
    }

    override fun setRefreshToken(token: String) {
        runBlocking { dataStore.saveRefreshToken(token) }
    }
}