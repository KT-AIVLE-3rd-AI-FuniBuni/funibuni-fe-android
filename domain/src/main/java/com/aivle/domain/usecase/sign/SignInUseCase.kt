package com.aivle.domain.usecase.sign

import com.aivle.domain.response.SignInResponse
import com.aivle.domain.model.sign.SignInUser
import com.aivle.domain.repository.SignRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class SignInUseCase @Inject constructor(
    private val repository: SignRepository
) {
    suspend operator fun invoke(signInUser: SignInUser): Flow<SignInResponse> {
        return repository.signIn(signInUser)
    }
}