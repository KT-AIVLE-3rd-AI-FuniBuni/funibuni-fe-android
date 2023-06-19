package com.aivle.domain.model

import com.aivle.domain.model.user.User

data class SharingPostItem(
    val postId: Int,
    val image_url: String,
    val title: String,
    val addressCity: String,
    val addressDistrict: String,
    val addressDong: String,
    val createdDate: String,
    val expiredDate: String,
    val user: User,
)