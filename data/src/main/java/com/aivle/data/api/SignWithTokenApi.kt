package com.aivle.data.api

import com.aivle.data.entity.token.AuthTokenEntity
import com.skydoves.sandwich.ApiResponse
import retrofit2.Call
import retrofit2.http.POST

interface SignWithTokenApi {

    @POST("user/auto-signin")
    suspend fun signInAuto(): ApiResponse<AuthTokenEntity>

    @POST("user/signout")
    suspend fun signOut(): ApiResponse<Map<String, String>>

    @POST("user/withdrawal")
    suspend fun withdrawal(): ApiResponse<Map<String, String>>
}