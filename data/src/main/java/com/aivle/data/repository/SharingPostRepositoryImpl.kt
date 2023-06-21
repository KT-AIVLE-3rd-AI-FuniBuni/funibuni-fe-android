package com.aivle.data.repository

import com.aivle.data._util.SampleData
import com.aivle.data.service.SharingPostService
import com.aivle.domain.model.sharingPost.SharingPost
import com.aivle.domain.model.sharingPost.SharingPostDetail
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.domain.repository.SharingPostRepository
import com.aivle.domain.response.DataResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SharingPostRepositoryImpl @Inject constructor(
    private val service: SharingPostService
) : SharingPostRepository {

    override suspend fun getSharingPostList(): Flow<DataResponse<List<SharingPostItem>>> = flow {
        val data = SampleData.getSharingPostItems(20)
        emit(DataResponse.Success(data))
    }

    override suspend fun getSharingPostDetail(postId: Int): Flow<DataResponse<SharingPostDetail>> = flow {
        val data = SampleData.getSharingPostDetail()
        emit(DataResponse.Success(data))
    }
}