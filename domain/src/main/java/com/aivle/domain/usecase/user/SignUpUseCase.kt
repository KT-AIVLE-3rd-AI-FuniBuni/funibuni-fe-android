package com.aivle.domain.usecase.user

import com.aivle.domain.model.user.SignUpResponse
import com.aivle.domain.model.user.UserForSignUp
import com.aivle.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: UserForSignUp): Flow<SignUpResponse> = repository.signUp(user)
}