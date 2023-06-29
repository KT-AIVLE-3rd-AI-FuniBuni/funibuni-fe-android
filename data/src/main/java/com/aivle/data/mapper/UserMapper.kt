package com.aivle.data.mapper

import com.aivle.data.entity.user.SignInUserEntity
import com.aivle.data.entity.user.SignUpUserEntity
import com.aivle.data.entity.user.UserEntity
import com.aivle.domain.model.sign.SignInUser
import com.aivle.domain.model.sign.SignUpUser
import com.aivle.domain.model.user.User

fun UserEntity.toModel(): User = User(user_id, phone_number, name, nickname)
fun User.toEntity(): UserEntity = UserEntity(id, phoneNumber, name, nickname)

fun SignUpUser.toEntity() = SignUpUserEntity(
    phoneNumber,
    name,
    kakaoAddress.road_address!!.zone_no,
    kakaoAddress.road_address!!.address_name,
    kakaoAddress.land_address?.address_name ?: kakaoAddress.road_address!!.address_name,
    kakaoAddress.road_address!!.region_1depth_name,
    kakaoAddress.road_address!!.region_2depth_name,
    kakaoAddress.road_address!!.region_3depth_name,
    addressDetail,
)

fun SignInUser.toEntity() = SignInUserEntity(
    phoneNumber,
)