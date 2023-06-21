package com.aivle.data.mapper

import com.aivle.data.entity.user.UserEntity
import com.aivle.domain.model.user.User

fun UserEntity.toModel(): User = User(user_id, phone_number, name, nickname)
fun User.toEntity(): UserEntity = UserEntity(id, phoneNumber, name, nickname)