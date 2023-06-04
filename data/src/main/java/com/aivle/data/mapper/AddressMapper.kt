package com.aivle.data.mapper

import com.aivle.data.entity.AddressEntity
import com.aivle.domain.model.Address

fun AddressEntity.toModel(): Address = Address(value)
fun Address.toEntity(): AddressEntity = AddressEntity(value)