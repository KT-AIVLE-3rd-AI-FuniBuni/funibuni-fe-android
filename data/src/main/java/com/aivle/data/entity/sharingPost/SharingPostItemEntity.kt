package com.aivle.data.entity.sharingPost

import com.aivle.data.entity.user.UserEntity

data class SharingPostItemEntity(
    val post_id: Int,
    val user: UserEntity,
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
    val created_at: String,
    val is_sharing: Boolean,
    val comments_count: Int,
    val likes_count: Int,
)