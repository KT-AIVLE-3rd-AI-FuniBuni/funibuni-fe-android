package com.aivle.domain.model.sharingPost

import com.aivle.domain.model.user.User

data class Reply(
    val replyId: Int,
    val commentId: Int,
    val user: User,
    val reply: String,
    val createdAt: String,
)
