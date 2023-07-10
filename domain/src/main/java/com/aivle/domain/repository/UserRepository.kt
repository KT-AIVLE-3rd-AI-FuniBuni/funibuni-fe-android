package com.aivle.domain.repository

import com.aivle.domain.model.address.Address
import com.aivle.domain.model.user.User
import com.aivle.domain.response.DataResponse
import com.aivle.domain.response.NothingResponse
import com.aivle.domain.usecase.user.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUserInfo(): Flow<DataResponse<UserInfo>>

    suspend fun updateUserInfo(user: User): Flow<DataResponse<User>>

    suspend fun getAddresses(): Flow<DataResponse<List<Address>>>
}