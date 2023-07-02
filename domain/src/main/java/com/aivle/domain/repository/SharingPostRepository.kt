package com.aivle.domain.repository

import com.aivle.domain.model.sharingPost.Comment
import com.aivle.domain.model.sharingPost.Reply
import com.aivle.domain.model.sharingPost.SharingPostCreate
import com.aivle.domain.model.sharingPost.SharingPostDetail
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.domain.response.DataResponse
import com.aivle.domain.response.NothingResponse
import kotlinx.coroutines.flow.Flow

interface SharingPostRepository {

    suspend fun createPost(post: SharingPostCreate): Flow<DataResponse<SharingPostItem>>

    suspend fun getPosts(district: String): Flow<DataResponse<List<SharingPostItem>>>

    suspend fun getPostDetail(post_id: Int): Flow<DataResponse<SharingPostDetail>>

    suspend fun updatePost(post: SharingPostDetail): Flow<NothingResponse>

    suspend fun deletePost(post_id: Int): Flow<NothingResponse>

    suspend fun completeSharingPost(post_id: Int): Flow<NothingResponse> // 나눔 완료

    suspend fun reportPost(post_id: Int): Flow<NothingResponse>

    suspend fun likePost(post_id: Int): Flow<NothingResponse>

    suspend fun unlikePost(post_id: Int): Flow<NothingResponse>

    suspend fun addComment(post_id: Int, comment: String): Flow<DataResponse<Comment>>

    suspend fun updateComment(comment: Comment): Flow<NothingResponse>

    suspend fun deleteComment(post_id: Int, comment_id: Int): Flow<NothingResponse>

    suspend fun reportComment(post_id: Int, comment_id: Int): Flow<NothingResponse>

    suspend fun getReplies(post_id: Int, comment_id: Int): Flow<DataResponse<List<Reply>>>

    suspend fun addReply(post_id: Int, comment_id: Int, reply: String): Flow<DataResponse<Reply>>

    suspend fun updateReply(post_id: Int, reply: Reply): Flow<NothingResponse>

    suspend fun deleteReply(post_id: Int, comment_id: Int, reply_id: Int): Flow<NothingResponse>

    suspend fun reportReply(post_id: Int, comment_id: Int, reply_id: Int): Flow<NothingResponse>
}