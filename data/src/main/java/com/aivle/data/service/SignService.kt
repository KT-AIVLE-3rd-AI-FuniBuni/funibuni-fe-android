package com.aivle.data.service

import com.aivle.data.entity.token.WebTokenEntity
import com.aivle.domain.model.sign.SignIn
import com.aivle.domain.model.sign.SignUp
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.POST

interface SignService {

    @POST("api/user/signup")
    suspend fun signUp(signUp: SignUp): ApiResponse<WebTokenEntity>

    @POST("api/user/signin")
    suspend fun signIn(signIn: SignIn): ApiResponse<WebTokenEntity>

    @POST("api/user/auto-signin")
    suspend fun signInAuto(): ApiResponse<String>

    @POST("api/user/signout")
    suspend fun signOut(): ApiResponse<String>

    @POST("api/user/withdrawal")
    suspend fun withdrawal(): ApiResponse<String>
}