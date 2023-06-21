package com.aivle.data.mapper

import com.aivle.data.entity.address.AddressEntity
import com.aivle.domain.model.address.Address

fun AddressEntity.toModel(): Address = Address(value)
fun Address.toEntity(): AddressEntity = AddressEntity(value)