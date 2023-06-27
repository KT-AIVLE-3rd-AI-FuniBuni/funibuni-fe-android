package com.aivle.domain.repository

interface RefreshTokenRepository {

    fun getRefreshToken(): String?
    fun setRefreshToken(token: String)
}