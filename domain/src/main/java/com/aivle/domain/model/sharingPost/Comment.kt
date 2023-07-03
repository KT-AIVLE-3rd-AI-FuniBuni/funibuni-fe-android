package com.aivle.domain.model.sharingPost

import com.aivle.domain.model.user.User

data class Comment constructor(
    val comment_id: Int,
    val post_id: Int,
    val user: User,
    val comment: String,
    val reply_count: Int,
    val created_at: String,
) {
    var onClick: ((comment: Comment) -> Unit)? = null

    val created_at_string: String? = created_at.split("T").firstOrNull()
}
