package com.aivle.domain.model.sharingPost

import com.aivle.domain.model.user.User

data class SharingPostDetail(
    val post_id: Int,
    val user: User,
    val address_city: String,
    val address_district: String,
    val address_dong: String,
    val image_url: String,
    val product_top_category: String,
    val product_mid_category: String,
    val product_low_category: String,
    val title: String,
    val content: String,
    val is_sharing: Boolean,
    val comments_count: Int,
    val likes_count: Int,
    val comments: List<Comment>,
    val created_at: String,
    val expired_date: String,
)
