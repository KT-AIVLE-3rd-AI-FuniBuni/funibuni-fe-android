package com.aivle.domain.usecase.sign

import com.aivle.domain.response.SignInResponse
import com.aivle.domain.model.sign.SignIn
import com.aivle.domain.repository.SignRepository
import com.aivle.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class SignInUseCase @Inject constructor(
    private val repository: SignRepository
) {
    suspend operator fun invoke(signIn: SignIn): Flow<SignInResponse> {
        return repository.signIn(signIn)
    }
}