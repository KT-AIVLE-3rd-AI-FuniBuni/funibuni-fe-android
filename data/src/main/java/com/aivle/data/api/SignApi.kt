package com.aivle.data.api

import com.aivle.data.entity.token.WebTokenEntity
import com.aivle.domain.model.sign.SignInUser
import com.aivle.domain.model.sign.SignUpUser
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.POST

interface SignApi {

    @POST("user/signup")
    suspend fun signUp(signUpUser: SignUpUser): ApiResponse<WebTokenEntity>

    @POST("user/signin")
    suspend fun signIn(signInUser: SignInUser): ApiResponse<WebTokenEntity>
}