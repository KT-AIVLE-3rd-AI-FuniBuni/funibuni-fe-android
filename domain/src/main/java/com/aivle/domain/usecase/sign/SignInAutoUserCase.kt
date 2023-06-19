package com.aivle.domain.usecase.sign

import com.aivle.domain.repository.SignRepository
import com.aivle.domain.response.SignInAutoResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInAutoUserCase @Inject constructor(
    private val repository: SignRepository
) {
    suspend operator fun invoke(refreshToken: String): Flow<SignInAutoResponse> {
        return repository.signInAuto(refreshToken)
    }
}