package com.aivle.data.repository

import com.aivle.data.api.UserApi
import com.aivle.data.di.api.FurniBurniApiProvider
import com.aivle.domain.model.user.User
import com.aivle.domain.repository.UserRepository
import com.aivle.domain.response.DataResponse
import com.aivle.domain.response.NothingResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @FurniBurniApiProvider private val service: UserApi
) : UserRepository {

    override suspend fun getUserInfo(): Flow<DataResponse<User>> = flow {
        // service.getUserInfo()
        emit(DataResponse.Success(User(0, "010-0000-0000", "홍길동", "홍버니")))
    }

    override suspend fun updateUserInfo(user: User): Flow<NothingResponse> = flow {
        // service.updateUserInfo(user)
        emit(NothingResponse.Success)
    }
}