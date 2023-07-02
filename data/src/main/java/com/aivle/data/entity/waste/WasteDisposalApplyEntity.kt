package com.aivle.data.entity.waste

data class WasteDisposalApplyEntity(
    val waste_id: Int,
    val waste_spec_id: Int,
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
)