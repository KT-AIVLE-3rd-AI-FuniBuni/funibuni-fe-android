package com.aivle.data.entity.address

data class AddressEntity(
    val address_id: Int,
    val postal_code: String,
    val address_road: String,
    val address_land: String,
    val address_city: String,
    val address_district: String,
    val address_dong: String,
    val address_detail: String,
    val disposal_location: String?,
)