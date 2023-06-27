package com.aivle.data.mapper

import com.aivle.data.AddressProto
import com.aivle.domain.model.address.Address

fun AddressProto.toModel() =
    Address(addressId.toInt(), postalCode, roadAddress, landAddress, city, district, dong, disposalLocation)