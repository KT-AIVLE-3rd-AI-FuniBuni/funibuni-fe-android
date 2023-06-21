package com.aivle.domain.model.sharingPost

import com.aivle.domain.model.user.User

data class Comment constructor(
    val commentId: Int,
    val postId: Int,
    val user: User,
    val comment: String,
    val replyCount: Int,
    val createdAt: String,
)
