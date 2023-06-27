package com.aivle.domain.model.address

import com.aivle.domain.model.user.User

data class Address(
    val addressId: Int,
    val user: User,
    val postalCode: String,
    val roadAddress: String,
    val lendAddress: String,
    val city: String,
    val district: String,
    val dong: String,
)