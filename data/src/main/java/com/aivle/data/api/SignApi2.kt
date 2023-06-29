package com.aivle.data.api

import com.aivle.data.entity.token.AuthTokenEntity
import com.aivle.data.entity.user.SignUpUserEntity
import com.aivle.domain.model.sign.SignInUser
import com.aivle.domain.model.sign.SignUpUser
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.POST

interface SignApi2 {

    @POST("user/signup")
    suspend fun signUp(signUpUser: SignUpUserEntity): ApiResponse<AuthTokenEntity>

    @POST("user/signin")
    suspend fun signIn(signInUser: SignInUser): ApiResponse<AuthTokenEntity>

    @POST("user/signin")
    suspend fun signIn2(): Call<AuthTokenEntity>
}