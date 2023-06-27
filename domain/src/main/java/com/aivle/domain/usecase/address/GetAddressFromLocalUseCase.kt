package com.aivle.domain.usecase.address

import com.aivle.domain.model.address.Address
import com.aivle.domain.repository.AddressLocalRepository
import javax.inject.Inject

class GetAddressFromLocalUseCase @Inject constructor(
    private val repository: AddressLocalRepository
) {
    suspend operator fun invoke(): Address = repository.getAddress()
}