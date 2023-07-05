package com.aivle.data.mapper

import com.aivle.data.entity.mybuni.MyBuniEntity
import com.aivle.domain.model.mybuni.MyBuni

/* MyBuniEntity */
fun MyBuniEntity.toModel() = MyBuni(
    user.toModel(), posts.map { it.toModel() }, waste_applies.map { it.toModel() },
)