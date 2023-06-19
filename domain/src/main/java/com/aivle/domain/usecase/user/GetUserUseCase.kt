package com.aivle.domain.usecase.user

import com.aivle.domain.model.user.User
import com.aivle.domain.repository.UserRepository
import com.aivle.domain.response.GeneralResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Flow<GeneralResponse<User>> = repository.getUserInfo()
}