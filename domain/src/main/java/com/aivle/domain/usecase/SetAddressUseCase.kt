package com.aivle.domain.usecase

import com.aivle.domain.model.Address
import com.aivle.domain.repository.AddressRepository
import javax.inject.Inject

class SetAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(address: Address): Unit = repository.setAddress(address)
}