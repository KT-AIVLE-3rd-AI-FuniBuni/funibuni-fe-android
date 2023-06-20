package com.aivle.domain.model

import com.aivle.domain.model.user.User

data class SharingPost constructor(
    val postId: String,
    val user: User,
    val address: String,
    val title: String,
    val productCategory: String,
    val content: String,
    val createdDate: String,
    val expireDate: String,
)