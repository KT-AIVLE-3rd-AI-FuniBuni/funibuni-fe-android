package com.aivle.domain.model.waste

import com.aivle.domain.model.user.User
import com.aivle.domain.model.util.DatetimeUtil

data class WasteDisposalApplyItem(
    val waste_id: Int,
    val apply_binary: Int, // 신청 플래그(0:대기, 1:신청완료)
    val user: User,
    val waste_spec: WasteSpec,
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
) {
    val address_road_detail_two_lines: String = "${address_full_street}\n${address_detail}"
    val disposal_datetime_formatting_two_lines: String? = DatetimeUtil.formatDatetimeFullStringTwoLines(disposal_datetime)

    var onClick: ((wasteId: Int) -> Unit)? = null
}