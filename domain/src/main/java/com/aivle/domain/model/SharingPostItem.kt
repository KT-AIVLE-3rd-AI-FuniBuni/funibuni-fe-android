package com.aivle.domain.model

import com.aivle.domain.model.user.User

data class SharingPostItem constructor(
    val postId: Int,
    val user: User,
    val image_url: String,
    val productTopCategory: String,
    val productMidCategory: String,
    val productLowCategory: String,
    val title: String,
    val addressCity: String,
    val addressDistrict: String,
    val addressDong: String,
    val createdDate: String,
    val expiredDate: String,
    val commentCount: Int,
    val likeCount: Int,
) {
    val addressAndDate: String = "$addressDistrict $addressDong · $createdDate"

    var onClick: ((postId: Int) -> Unit)? = null
}