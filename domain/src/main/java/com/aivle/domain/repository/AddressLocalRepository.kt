package com.aivle.domain.repository

import com.aivle.domain.model.address.Address

interface AddressLocalRepository {

    suspend fun getAddress(): Address
    suspend fun setAddress(address: Address)
}