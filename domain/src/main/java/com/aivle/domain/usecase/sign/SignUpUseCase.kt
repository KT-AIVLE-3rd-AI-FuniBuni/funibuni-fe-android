package com.aivle.domain.usecase.sign

import com.aivle.domain.response.SignUpResponse
import com.aivle.domain.model.sign.SignUp
import com.aivle.domain.repository.SignRepository
import com.aivle.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: SignRepository
) {
    suspend operator fun invoke(user: SignUp): Flow<SignUpResponse> = repository.signUp(user)
}