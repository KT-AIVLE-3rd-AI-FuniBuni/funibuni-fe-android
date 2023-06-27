package com.aivle.domain.model.address

data class Address(
    val addressId: Int,
    val postalCode: String,
    val roadAddress: String,
    val lendAddress: String,
    val city: String,
    val district: String,
    val dong: String,
    val disposalLocation: String,
)