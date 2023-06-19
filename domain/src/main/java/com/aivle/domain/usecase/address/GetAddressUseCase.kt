package com.aivle.domain.usecase.address

import com.aivle.domain.model.Address
import com.aivle.domain.repository.AddressRepository
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(): Address? = repository.getAddress()
}