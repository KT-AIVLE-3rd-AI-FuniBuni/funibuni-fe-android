package com.aivle.data.datastore

import com.aivle.data.AddressProto
import com.aivle.domain.model.address.Address
import kotlinx.coroutines.flow.Flow

interface AddressDatastore {

    val addressFlow: Flow<AddressProto>

    suspend fun setAddress(address: Address)
}