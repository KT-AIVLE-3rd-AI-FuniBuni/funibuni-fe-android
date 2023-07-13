package com.aivle.data.api

import com.aivle.data.di.api.ApiConstants
import com.aivle.data.entity.token.AuthTokenEntity
import com.aivle.data.entity.user.SignInUserEntity
import com.aivle.data.entity.user.SignUpResultEntity
import com.aivle.data.entity.user.SignUpUserEntity
import com.skydoves.sandwich.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SignApi {

    @POST("user/signup")
    suspend fun signUp(@Body signUpUser: SignUpUserEntity): ApiResponse<SignUpResultEntity>

    @POST("user/signin")
    suspend fun signIn(@Body signInUser: SignInUserEntity): ApiResponse<AuthTokenEntity>

    @POST("user/token-refresh")
    fun refreshAccessToken(
        @Header(ApiConstants.AUTHORIZATION) refreshToken: String
    ): Call<AuthTokenEntity>
}