package com.aivle.data.service

import com.aivle.data.entity.user.UserEntity
import com.aivle.domain.model.user.User
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserService {

    @GET("user/info")
    suspend fun getUserInfo(): ApiResponse<UserEntity>

    @PUT("user/info")
    suspend fun updateUserInfo(user: User): ApiResponse<String>
}