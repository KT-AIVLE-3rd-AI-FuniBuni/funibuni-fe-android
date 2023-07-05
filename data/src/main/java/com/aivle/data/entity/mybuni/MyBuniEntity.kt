package com.aivle.data.entity.mybuni

import com.aivle.data.entity.sharingPost.SharingPostItemEntity
import com.aivle.data.entity.user.UserEntity
import com.aivle.data.entity.waste.WasteDisposalApplyItemEntity

data class MyBuniEntity(
    val user: UserEntity,
    val posts: List<SharingPostItemEntity>,
    val waste_applies: List<WasteDisposalApplyItemEntity>,
)