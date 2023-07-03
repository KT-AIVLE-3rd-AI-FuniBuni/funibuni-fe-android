package com.aivle.data.entity.waste

import com.aivle.data.entity.user.UserEntity
import com.google.gson.annotations.SerializedName

data class WasteDisposalApplyItemEntity(
    val waste_id: Int,
    val apply_binary: Int, // 신청 플래그(0:대기, 1:신청완료)
    val user: UserEntity,
    @SerializedName("waste_spec_id")
    val waste_spec: WasteSpecEntity,
    val image_title: String,
    val image_url: String,
    val postal_code: String,
    val address_full_lend: String,
    val address_full_street: String,
    val address_city: String,
    val address_district: String,
    val address_dong: String,
    val address_detail: String,
    val disposal_location: String?,
    val disposal_datetime: String,
    val memo: String,
    val created_at: String,
)