package com.aivle.domain.usecase.user

import com.aivle.domain.model.user.User
import com.aivle.domain.repository.UserRepository
import com.aivle.domain.response.GeneralResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User): Flow<GeneralResponse<User>> {
        return repository.updateUserInfo(user)
    }
}