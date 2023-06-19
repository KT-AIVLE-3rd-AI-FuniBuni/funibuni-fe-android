package com.aivle.domain.model.user

data class User constructor(
    val id: Long,
    val phoneNumber: String,
    val name: String,
    val nickname: String,
)