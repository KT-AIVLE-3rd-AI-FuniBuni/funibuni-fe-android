package com.aivle.domain.repository

import com.aivle.domain.model.sharingPost.Reply
import com.aivle.domain.model.sharingPost.SharingPostDetail
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.domain.response.DataResponse
import kotlinx.coroutines.flow.Flow

interface SharingPostRepository {

    suspend fun getSharingPostList(): Flow<DataResponse<List<SharingPostItem>>>

    suspend fun getSharingPostDetail(postId: Int): Flow<DataResponse<SharingPostDetail>>

    suspend fun getReplyList(commentId: Int): Flow<DataResponse<List<Reply>>>
}