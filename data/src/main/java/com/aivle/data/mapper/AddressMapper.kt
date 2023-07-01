package com.aivle.data.mapper

import com.aivle.data.AddressProto
import com.aivle.data.entity.address.AddressEntity
import com.aivle.domain.model.address.Address

fun AddressProto.toModel() =
    Address(addressId.toInt(), postalCode, roadAddress, landAddress, city, district, dong, disposalLocation)

fun AddressEntity.toModel() =
    Address(address_id, postal_code, address_road, address_land, address_city, address_district, address_dong, disposal_location ?: "")