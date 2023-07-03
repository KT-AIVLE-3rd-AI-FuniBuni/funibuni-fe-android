package com.aivle.domain.usecase.user

import com.aivle.domain.model.address.Address
import com.aivle.domain.model.user.User

data class UserInfo(
    val user: User,
    val address: Address,
)