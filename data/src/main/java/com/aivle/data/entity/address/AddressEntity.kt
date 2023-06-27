package com.aivle.data.entity.address

data class AddressEntity(
    val address_id: Int,
    val user_id: Int,
    val postal_code: Int,
    val address_road: String,
    val address_land: String,
    val address_city: String,
    val address_district: String,
    val address_dong: String,
)