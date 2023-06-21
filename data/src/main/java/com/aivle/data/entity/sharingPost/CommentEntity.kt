package com.aivle.data.entity.sharingPost

import com.aivle.data.entity.user.UserEntity

data class CommentEntity(
    val comment_id: Int,
    val post_id: Int,
    val user: UserEntity,
    val comment: String,
    val reply_count: Int,
    val created_at: String,
)
