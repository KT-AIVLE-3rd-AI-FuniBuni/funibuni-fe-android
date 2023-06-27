package com.aivle.domain.usecase.token

import com.aivle.domain.repository.RefreshTokenRepository
import javax.inject.Inject

class GetRefreshTokenUseCase @Inject constructor(
    private val repository: RefreshTokenRepository
) {
    operator fun invoke() = repository.getRefreshToken()
}