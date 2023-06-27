package com.aivle.domain.usecase.sign

import com.aivle.domain.repository.SignRepository
import javax.inject.Inject

class SignInWithTokenUseCase @Inject constructor(
    private val repository: SignRepository
) {
    suspend operator fun invoke() = repository.signInWithToken()
}