package com.aivle.data.repository

import com.aivle.data.service.SharingPostService
import com.aivle.domain.model.SharingPostItem
import com.aivle.domain.model.user.User
import com.aivle.domain.repository.SharingPostRepository
import com.aivle.domain.response.DataResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SharingPostRepositoryImpl @Inject constructor(
    private val service: SharingPostService
) : SharingPostRepository {

    override fun getSharingPostList(): Flow<DataResponse<List<SharingPostItem>>> = flow {
        val data = List(10) { i ->
            SharingPostItem(i, "", "Sharing Post Title $i", "Seoul", "송파구", "잠실${i}동", "", "", User(i, "010-0000-0000", "HongGilDong", "HongBurni"))
        }
        emit(DataResponse.Success(data))
    }
}