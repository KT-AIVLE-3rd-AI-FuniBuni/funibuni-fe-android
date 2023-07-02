package com.aivle.domain.model.sharingPost

data class SharingPostCreate(
    val title: String,
    val content: String,
    val expired_date: String,
    val image_url: String,
    val product_top_category: String,
    val product_mid_category: String,
    val product_low_category: String,
    val address_city: String,
    val address_district: String,
    val address_dong: String,
)