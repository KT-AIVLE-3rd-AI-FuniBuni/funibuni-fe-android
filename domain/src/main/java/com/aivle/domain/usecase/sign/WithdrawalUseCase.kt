package com.aivle.domain.usecase.sign

import com.aivle.domain.repository.SignRepository
import com.aivle.domain.repository.UserRepository
import com.aivle.domain.response.GeneralResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WithdrawalUseCase @Inject constructor(
    private val repository: SignRepository
) {
    suspend operator fun invoke(): Flow<GeneralResponse<Nothing>> = repository.withdrawal()
}