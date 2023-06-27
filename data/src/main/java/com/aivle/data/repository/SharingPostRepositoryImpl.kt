package com.aivle.data.repository

import com.aivle.data._util.SampleData
import com.aivle.data.api.SharingPostApi
import com.aivle.data.di.api.FuniBuniApiQualifier
import com.aivle.domain.model.sharingPost.Reply
import com.aivle.domain.model.sharingPost.SharingPostDetail
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.domain.repository.SharingPostRepository
import com.aivle.domain.response.DataResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SharingPostRepositoryImpl @Inject constructor(
    @FuniBuniApiQualifier private val api: SharingPostApi
) : SharingPostRepository {

    override suspend fun getSharingPostList(): Flow<DataResponse<List<SharingPostItem>>> = flow {
        val data = SampleData.getSharingPostItems(20)
        emit(DataResponse.Success(data))
    }

    override suspend fun getSharingPostDetail(postId: Int): Flow<DataResponse<SharingPostDetail>> = flow {
        val data = SampleData.getSharingPostDetail()
        emit(DataResponse.Success(data))
    }

    override suspend fun getReplyList(commentId: Int): Flow<DataResponse<List<Reply>>> = flow {
        val data = SampleData.getReplies(commentId)
        emit(DataResponse.Success(data))
    }
}