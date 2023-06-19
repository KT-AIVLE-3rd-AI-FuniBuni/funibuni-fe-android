package com.aivle.data.repository

import com.aivle.data.service.UserService
import com.aivle.domain.model.user.User
import com.aivle.domain.repository.UserRepository
import com.aivle.domain.response.GeneralResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val service: UserService
) : UserRepository {



    override suspend fun getUserInfo(): Flow<GeneralResponse<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserInfo(user: User): Flow<GeneralResponse<Nothing>> {
        TODO("Not yet implemented")
    }
}