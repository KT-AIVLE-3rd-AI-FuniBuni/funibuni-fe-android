package com.aivle.data.mapper

import com.aivle.data.AddressProto
import com.aivle.data.entity.address.AddressEntity
import com.aivle.domain.model.address.Address

fun AddressProto.toModel() =
    Address(addressId.toInt(), postalCode, roadAddress, landAddress, city, district, dong, detail, disposalLocation)

fun AddressEntity.toModel() =
    Address(address_id, postal_code, address_road, address_land, address_city, address_district, address_dong, address_detail, disposal_location ?: "")

fun Address.toEntity() =
    AddressEntity(addressId, postalCode, roadAddress, landAddress, city, district, dong, detail, disposalLocation)