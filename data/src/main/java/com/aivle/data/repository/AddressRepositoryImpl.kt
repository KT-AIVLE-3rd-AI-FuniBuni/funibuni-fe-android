package com.aivle.data.repository

import com.aivle.data.datastore.PreferencesDataStore
import com.aivle.data.mapper.toEntity
import com.aivle.data.mapper.toModel
import com.aivle.domain.model.Address
import com.aivle.domain.repository.AddressRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val dataStore: PreferencesDataStore
) : AddressRepository {

    override suspend fun getAddress(): Address? {
        return dataStore.address.first()?.toModel()
    }

    override suspend fun setAddress(address: Address) {
        dataStore.setAddress(address.toEntity())
    }
}