package com.aivle.domain.model.sign

import com.aivle.domain.model.kakao.KakaoAddressDocument

data class SignUpUser constructor(
    val phoneNumber: String,
    val name: String,
    val kakaoAddress: KakaoAddressDocument,
    val addressDetail: String,
)