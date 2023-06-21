package com.aivle.domain.model.sharingPost

data class SharingPostDetail(
    val post: SharingPost,
    val comments: List<Comment>,
)
