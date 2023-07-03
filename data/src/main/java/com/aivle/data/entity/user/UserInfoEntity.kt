package com.aivle.data.entity.user

import com.aivle.data.entity.address.AddressEntity

data class UserInfoEntity(
    val user: UserEntity,
    val address: AddressEntity,
)