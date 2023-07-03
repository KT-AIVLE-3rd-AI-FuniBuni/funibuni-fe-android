package com.aivle.data.api

import com.aivle.data.entity.user.UserEntity
import com.aivle.data.entity.user.UserInfoEntity
import com.aivle.domain.model.user.User
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApi {

    @GET("user/info")
    suspend fun getUserInfo(): ApiResponse<UserInfoEntity>

    @PUT("user/info")
    suspend fun updateUserInfo(user: User): ApiResponse<UserEntity>
}