package com.aivle.data.mapper

import com.aivle.data.entity.sharingPost.CommentEntity
import com.aivle.data.entity.sharingPost.ReplyEntity
import com.aivle.data.entity.sharingPost.SharingPostCreateEntity
import com.aivle.data.entity.sharingPost.SharingPostDetailEntity
import com.aivle.data.entity.sharingPost.SharingPostItemEntity
import com.aivle.domain.model.sharingPost.Comment
import com.aivle.domain.model.sharingPost.Reply
import com.aivle.domain.model.sharingPost.SharingPost
import com.aivle.domain.model.sharingPost.SharingPostCreate
import com.aivle.domain.model.sharingPost.SharingPostDetail
import com.aivle.domain.model.sharingPost.SharingPostItem

/* SharingPost Create */
fun SharingPostCreate.toEntity() =
    SharingPostCreateEntity(title, content, expired_date, image_url, product_top_category, product_mid_category, product_low_category, address_city, address_district, address_dong)

/* SharingPostItem */
fun SharingPostItemEntity.toModel() =
    SharingPostItem(post_id, user.toModel(), title, content, expired_date, image_url, product_top_category, product_mid_category, product_low_category, address_city, address_district, address_dong, created_at, is_sharing, comments_count, likes_count)

/* SharingPostDetail */
fun SharingPostDetailEntity.toModel(): SharingPostDetail =
    SharingPostDetail(post_id, user.toModel(), address_city, address_district, address_dong, image_url, product_top_category, product_mid_category, product_low_category, title, content, is_sharing, comments_count, likes_count, comments.map { it.toModel() }, created_at, expired_date)
fun SharingPostDetail.toEntity() =
    SharingPostDetailEntity(post_id, user.toEntity(), address_city, address_district, address_dong, image_url, product_top_category, product_mid_category, product_low_category, title, content, is_sharing, comments_count, likes_count, comments.map { it.toEntity() }, created_at, expired_date)

/* Comment */
fun CommentEntity.toModel(): Comment = Comment(comment_id, post_id, user.toModel(), comment, reply_count, created_at)
fun Comment.toEntity(): CommentEntity = CommentEntity(comment_id, post_id, user.toEntity(), comment, reply_count, created_at)

/* Reply */
fun ReplyEntity.toModel() =
    Reply(reply_id, user.toModel(), comment_id, reply, created_at)
fun Reply.toEntity() =
    ReplyEntity(reply_id, user.toEntity(), comment_id, reply, created_at)