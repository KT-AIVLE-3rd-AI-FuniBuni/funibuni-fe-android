package com.aivle.data.api

import com.aivle.data.entity.address.AddressEntity
import com.aivle.data.entity.user.UserEntity
import com.aivle.data.entity.user.UserInfoEntity
import com.aivle.domain.model.user.User
import com.aivle.domain.response.NothingResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserApi {

    @GET("user/info")
    suspend fun getUserInfo(): ApiResponse<UserInfoEntity>

    @PUT("user/info")
    suspend fun updateUserInfo(
        @Body body: Map<String, String>  // "nickname": "asdf"
    ): ApiResponse<UserEntity>

    @GET("user/address")
    suspend fun getAddresses(): ApiResponse<List<AddressEntity>>

    @POST("user/address")
    suspend fun addAddress(address: AddressEntity): ApiResponse<Map<String, String>>

    @DELETE("user/address")
    suspend fun deleteAddress(
        @Body body: Map<String, Int>  // "address_id": <id>
    ): ApiResponse<Map<String, String>>
}