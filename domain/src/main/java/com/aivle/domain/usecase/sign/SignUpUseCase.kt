package com.aivle.domain.usecase.sign

import com.aivle.domain.response.SignUpResponse
import com.aivle.domain.model.sign.SignUpUser
import com.aivle.domain.repository.SignRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: SignRepository
) {
    suspend operator fun invoke(user: SignUpUser): Flow<SignUpResponse> = repository.signUp(user)
}