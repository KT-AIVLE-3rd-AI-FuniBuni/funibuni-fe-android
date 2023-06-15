package com.aivle.domain.model.user

data class User(
    val id: Long,
    val userId: String,
    val userPassword: String,
    val phoneNumber: String,
    val name: String,
)