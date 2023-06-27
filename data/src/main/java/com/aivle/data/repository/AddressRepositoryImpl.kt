package com.aivle.data.repository

import com.aivle.domain.model.address.Address
import com.aivle.domain.repository.AddressRepository
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
//    private val addressDao: AddressDao
) : AddressRepository {

    override suspend fun getAddress(): Address? {
        TODO()
    }

    override suspend fun setAddress(address: Address) {
        TODO()
    }
}