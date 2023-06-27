package com.aivle.data.api

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.POST

interface SignWithTokenApi {

    @POST("user/auto-signin")
    suspend fun signInAuto(): ApiResponse<String>

    @POST("user/signout")
    suspend fun signOut(): ApiResponse<String>

    @POST("user/withdrawal")
    suspend fun withdrawal(): ApiResponse<String>
}