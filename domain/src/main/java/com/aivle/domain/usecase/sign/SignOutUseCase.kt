package com.aivle.domain.usecase.sign

import com.aivle.domain.repository.SignRepository
import com.aivle.domain.response.NothingResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: SignRepository
) {
    suspend operator fun invoke(): Flow<NothingResponse> = repository.signOut()
}