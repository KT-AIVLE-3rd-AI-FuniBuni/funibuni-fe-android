package com.aivle.domain.repository

interface WebTokenRepository {

    fun getRefreshToken(): String?
    fun setRefreshToken(token: String)

    fun getAccessToken(): String?
    fun setAccessToken(token: String)
}