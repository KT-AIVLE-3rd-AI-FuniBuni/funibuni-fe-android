package com.aivle.domain.model.sharingPost

import com.aivle.domain.model.user.User

data class SharingPost constructor(
    val postId: Int,
    val user: User,
    val address: String,
    val imageUrl: String,
    val title: String,
    val productCategory: String,
    val content: String,
    val createdDate: String,
    val expireDate: String,
)