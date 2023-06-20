package com.aivle.data._util

import com.aivle.domain.model.SharingPostItem
import com.aivle.domain.model.user.User
import kotlin.random.Random

object SampleData {

    fun getSharingPostItems(count: Int): List<SharingPostItem> {
        return List(count) { i ->
            SharingPostItem(i, User(i, "010-0000-0000", "HongGilDong", "HongBurni"), "", "가전제품", "TV", "42인치 이상", "Sharing Post Title $i", "서울", "송파구", "잠실${i}동", "6월 ${i}일", "", Random.nextInt(0, 100), Random.nextInt(0, 100))
        }
    }
}