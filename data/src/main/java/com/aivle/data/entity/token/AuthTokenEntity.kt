package com.aivle.data.entity.token

data class AuthTokenEntity(
    val refresh_token: String? = null,
    val access_token: String? = null,
)
