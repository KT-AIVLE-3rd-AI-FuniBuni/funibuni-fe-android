package com.aivle.data.mapper

import com.aivle.data.entity.sharingPost.CommentEntity
import com.aivle.data.entity.sharingPost.SharingPostDetailEntity
import com.aivle.domain.model.sharingPost.Comment
import com.aivle.domain.model.sharingPost.SharingPost
import com.aivle.domain.model.sharingPost.SharingPostDetail

internal fun SharingPostDetailEntity.toModel(): SharingPostDetail =
    SharingPostDetail(
        post = SharingPost(
            post_id,
            user.toModel(),
            "$address_district · $address_dong",
            image_url,
            title,
            product_mid_category,
            content,
            created_at,
            expired_date,
        ),
        comments = comments.map { it.toModel() },
    )

internal fun CommentEntity.toModel(): Comment = Comment(comment_id, post_id, user.toModel(), comment, reply_count, created_at)
internal fun Comment.toEntity(): CommentEntity = CommentEntity(commentId, postId, user.toEntity(), comment, replyCount, createdAt)