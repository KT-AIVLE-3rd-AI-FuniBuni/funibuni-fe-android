package com.aivle.data.entity.user

data class SignUpUserEntity(
    val phone_number: String,
    val name: String,
    val postal_code: String,
    val address_road: String,
    val address_land: String,
    val address_city: String,
    val address_district: String,
    val address_dong: String,
    val address_detail: String,
)