package com.aivle.domain.usecase.user

import com.aivle.domain.model.user.SignInResponse
import com.aivle.domain.model.user.UserForSignIn
import com.aivle.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class SignInUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userForSignIn: UserForSignIn): Flow<SignInResponse> {
        return repository.signIn(userForSignIn)
    }
}