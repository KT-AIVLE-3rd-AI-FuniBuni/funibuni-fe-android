package com.aivle.data.repository

import com.aivle.data.datastore.AddressDatastore
import com.aivle.data.mapper.toModel
import com.aivle.domain.model.address.Address
import com.aivle.domain.repository.AddressLocalRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AddressLocalRepositoryImpl @Inject constructor(
    private val datastore: AddressDatastore
) : AddressLocalRepository {

    override suspend fun getAddress(): Address {
        return datastore.addressFlow.first().toModel()
    }

    override suspend fun setAddress(address: Address) {
        datastore.setAddress(address)
    }
}