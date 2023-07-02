package com.aivle.domain.model.sharingPost

import com.aivle.domain.model.user.User

data class Reply(
    val reply_id: Int,
    val user: User,
    val comment_id: Int,
    val reply: String,
    val created_at: String,
)
