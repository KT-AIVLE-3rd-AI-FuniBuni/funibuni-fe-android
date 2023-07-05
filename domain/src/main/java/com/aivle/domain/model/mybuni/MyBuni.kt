package com.aivle.domain.model.mybuni

import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.domain.model.user.User
import com.aivle.domain.model.waste.WasteDisposalApplyItem

data class MyBuni(
    val user: User,
    val posts: List<SharingPostItem>,
    val waste_applies: List<WasteDisposalApplyItem>,
)