package com.aivle.domain.repository

import com.aivle.domain.model.user.User
import com.aivle.domain.response.GeneralResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUserInfo(): Flow<GeneralResponse<User>>

    suspend fun updateUserInfo(user: User): Flow<GeneralResponse<Nothing>>
}