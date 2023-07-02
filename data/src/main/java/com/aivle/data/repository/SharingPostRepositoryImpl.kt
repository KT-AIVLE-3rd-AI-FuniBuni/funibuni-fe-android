package com.aivle.data.repository

import com.aivle.data.api.SharingPostApi
import com.aivle.data.di.api.FuniBuniApiQualifier
import com.aivle.data.mapper.toEntity
import com.aivle.data.mapper.toModel
import com.aivle.domain.model.sharingPost.Comment
import com.aivle.domain.model.sharingPost.Reply
import com.aivle.domain.model.sharingPost.SharingPostCreate
import com.aivle.domain.model.sharingPost.SharingPostDetail
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.aivle.domain.repository.SharingPostRepository
import com.aivle.domain.response.DataResponse
import com.aivle.domain.response.NothingResponse
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SharingPostRepositoryImpl @Inject constructor(
    @FuniBuniApiQualifier private val api: SharingPostApi
) : SharingPostRepository {

    override suspend fun createPost(post: SharingPostCreate): Flow<DataResponse<SharingPostItem>> = flow {
        api.createPost(post.toEntity())
            .suspendOnSuccess {
                emit(DataResponse.Success(data.toModel()))
            }
            .suspendOnFailure {
                emit(DataResponse.Failure.Error(this))
            }
    }

    override suspend fun getPosts(district: String): Flow<DataResponse<List<SharingPostItem>>> = flow {
        api.getPosts(district)
            .suspendOnSuccess {
                emit(DataResponse.Success(data.map { it.toModel() }))
            }
            .suspendOnFailure {
                emit(DataResponse.Failure.Error(this))
            }
    }

    override suspend fun getPostDetail(post_id: Int): Flow<DataResponse<SharingPostDetail>> = flow {
        api.getPostDetail(post_id)
            .suspendOnSuccess {
                emit(DataResponse.Success(data.toModel()))
            }
            .suspendOnFailure {
                emit(DataResponse.Failure.Error(this))
            }
    }

    override suspend fun updatePost(post: SharingPostDetail): Flow<NothingResponse> = flow {
        api.updatePost(post.post_id, post.toEntity())
            .suspendOnSuccess { emit(NothingResponse.Success) }
            .suspendOnFailure { emit(NothingResponse.Failure.Error(this)) }
    }

    override suspend fun deletePost(post_id: Int): Flow<NothingResponse> = flow {
        api.deletePost(post_id)
            .suspendOnSuccess { emit(NothingResponse.Success) }
            .suspendOnFailure { emit(NothingResponse.Failure.Error(this)) }
    }

    override suspend fun completeSharingPost(post_id: Int): Flow<NothingResponse> = flow {
        api.completeSharingPost(post_id)
            .suspendOnSuccess { emit(NothingResponse.Success) }
            .suspendOnFailure { emit(NothingResponse.Failure.Error(this)) }
    }

    override suspend fun reportPost(post_id: Int): Flow<NothingResponse> = flow {
        api.reportPost(post_id)
            .suspendOnSuccess { emit(NothingResponse.Success) }
            .suspendOnFailure { emit(NothingResponse.Failure.Error(this)) }
    }

    override suspend fun likePost(post_id: Int): Flow<NothingResponse> = flow {
        api.likePost(post_id)
            .suspendOnSuccess { emit(NothingResponse.Success) }
            .suspendOnFailure { emit(NothingResponse.Failure.Error(this)) }
    }

    override suspend fun unlikePost(post_id: Int): Flow<NothingResponse> = flow {
        api.unlikePost(post_id)
            .suspendOnSuccess { emit(NothingResponse.Success) }
            .suspendOnFailure { emit(NothingResponse.Failure.Error(this)) }
    }

    override suspend fun addComment(post_id: Int, comment: String): Flow<DataResponse<Comment>> = flow {
        api.addComment(post_id, mapOf("comment" to comment))
            .suspendOnSuccess {
                emit(DataResponse.Success(data.toModel()))
            }
            .suspendOnFailure {
                emit(DataResponse.Failure.Error(this))
            }

    }

    override suspend fun updateComment(comment: Comment): Flow<NothingResponse> = flow {
        api.updateComment(comment.post_id, comment.comment_id, mapOf("comment" to comment.comment))
            .suspendOnSuccess { emit(NothingResponse.Success) }
            .suspendOnFailure { emit(NothingResponse.Failure.Error(this)) }
    }

    override suspend fun deleteComment(
        post_id: Int,
        comment_id: Int
    ): Flow<NothingResponse> = flow {
        api.deleteComment(post_id, comment_id)
            .suspendOnSuccess { emit(NothingResponse.Success) }
            .suspendOnFailure { emit(NothingResponse.Failure.Error(this)) }
    }

    override suspend fun reportComment(
        post_id: Int,
        comment_id: Int
    ): Flow<NothingResponse> = flow {
        api.reportComment(post_id, comment_id)
            .suspendOnSuccess { emit(NothingResponse.Success) }
            .suspendOnFailure { emit(NothingResponse.Failure.Error(this)) }
    }

    override suspend fun getReplies(
        post_id: Int,
        comment_id: Int
    ): Flow<DataResponse<List<Reply>>> = flow {
        api.getReplies(post_id, comment_id)
            .suspendOnSuccess { emit(DataResponse.Success(data.map { it.toModel() })) }
            .suspendOnFailure { emit(DataResponse.Failure.Error(this)) }
    }

    override suspend fun addReply(
        post_id: Int,
        comment_id: Int,
        reply: String
    ): Flow<DataResponse<Reply>> = flow {
        api.addReply(post_id, comment_id, mapOf("reply" to reply))
            .suspendOnSuccess { emit(DataResponse.Success(data.toModel())) }
            .suspendOnFailure { emit(DataResponse.Failure.Error(this)) }
    }

    override suspend fun updateReply(post_id: Int, reply: Reply): Flow<NothingResponse> = flow {
        api.updateReply(post_id, reply.comment_id, reply.reply_id, mapOf("reply" to reply.reply))
            .suspendOnSuccess { emit(NothingResponse.Success) }
            .suspendOnFailure { emit(NothingResponse.Failure.Error(this)) }
    }

    override suspend fun deleteReply(
        post_id: Int,
        comment_id: Int,
        reply_id: Int
    ): Flow<NothingResponse> = flow {
        api.deleteReply(post_id, comment_id, reply_id)
            .suspendOnSuccess { emit(NothingResponse.Success) }
            .suspendOnFailure { emit(NothingResponse.Failure.Error(this)) }
    }

    override suspend fun reportReply(
        post_id: Int,
        comment_id: Int,
        reply_id: Int
    ): Flow<NothingResponse> = flow {
        api.reportReply(post_id, comment_id, reply_id)
            .suspendOnSuccess { emit(NothingResponse.Success) }
            .suspendOnFailure { emit(NothingResponse.Failure.Error(this)) }
    }
}