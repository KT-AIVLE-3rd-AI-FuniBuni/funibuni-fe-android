package com.aivle.domain.usecase.address

import com.aivle.domain.repository.UserRepository
import javax.inject.Inject

class GetAddressListFromRemoteUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.getAddresses()
}