package com.aivle.data.service

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.POST

interface UserService {

    @POST("")
    suspend fun signUp(): ApiResponse<Nothing>
}