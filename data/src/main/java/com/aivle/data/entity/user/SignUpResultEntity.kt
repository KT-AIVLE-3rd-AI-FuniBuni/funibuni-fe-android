package com.aivle.data.entity.user

import com.aivle.data.entity.address.AddressEntity

data class SignUpResultEntity(
    val refresh_token: String,
    val access_token: String,
    val address: AddressEntity,
)