package com.aivle.domain.repository

interface AccessTokenRepository {

    fun getAccessToken(): String?
    fun setAccessToken(token: String)
}