package com.aivle.data.entity.sharingPost

import com.aivle.data.entity.user.UserEntity

data class ReplyEntity(
    val reply_id: Int,
    val user: UserEntity,
    val comment_id: Int,
    val reply: String,
    val created_at: String,
)