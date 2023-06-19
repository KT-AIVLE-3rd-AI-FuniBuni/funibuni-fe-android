package com.aivle.data.service

import com.aivle.data.entity.user.UserEntity
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserService {

    @GET("api/user/info")
    suspend fun getUserInfo(): ApiResponse<UserEntity>

    @PUT("api/user/info")
    suspend fun updateUserInfo(): ApiResponse<String>
}