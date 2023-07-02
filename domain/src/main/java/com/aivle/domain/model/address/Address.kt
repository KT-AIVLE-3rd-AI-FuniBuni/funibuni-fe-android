package com.aivle.domain.model.address

data class Address constructor(
    val addressId: Int,
    val postalCode: String,
    val roadAddress: String,
    val landAddress: String,
    val city: String,
    val district: String,
    val dong: String,
    val detail: String,
    val disposalLocation: String,
)