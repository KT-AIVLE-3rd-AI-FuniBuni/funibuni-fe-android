package com.aivle.domain.repository

import com.aivle.domain.model.address.Address

interface AddressRepository {

    suspend fun getAddress(): Address?
    suspend fun setAddress(address: Address)
}