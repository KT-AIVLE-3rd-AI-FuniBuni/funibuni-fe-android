package com.aivle.domain.model.user

data class UserForSignUp(
    val userId: String,
    val userPassword: String,
    val name: String,
    val phoneNumber: String,
)