package com.aivle.domain.model.user

data class User constructor(
    val id: Int,
    val phoneNumber: String,
    val name: String,
    val nickname: String,
)