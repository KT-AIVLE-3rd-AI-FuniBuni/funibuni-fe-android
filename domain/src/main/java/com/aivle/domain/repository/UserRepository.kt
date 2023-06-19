package com.aivle.domain.repository

import com.aivle.domain.model.user.User
import com.aivle.domain.response.DataResponse
import com.aivle.domain.response.NothingResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUserInfo(): Flow<DataResponse<User>>

    suspend fun updateUserInfo(user: User): Flow<NothingResponse>
}