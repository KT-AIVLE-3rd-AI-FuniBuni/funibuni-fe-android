package com.aivle.domain.model

import com.aivle.domain.model.user.User

data class SharingPost(
    val postId: String,
    val title: String,
    val content: String,
    val createdDate: String,
    val expireDate: String,
    val user: User,
)