package com.aivle.data.api

import com.aivle.domain.model.sharingPost.SharingPost
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface SharingPostApi {

    @POST("posts-create1")
    suspend fun createPosts1(): ApiResponse<SharingPost>

    @POST("posts-create2")
    suspend fun createPosts2(): ApiResponse<SharingPost>

    @GET("posts")
    suspend fun getPosts(): ApiResponse<List<SharingPostItem>>

    @GET("posts/{post_id}")
    suspend fun getPost(): ApiResponse<SharingPost>

    @PUT("posts/{post_id}")
    suspend fun updatePost(): ApiResponse<String>

    @DELETE("posts/{post_id}")
    suspend fun deletePost(): ApiResponse<String>

    @POST("posts/{post_id}/report")
    suspend fun reportPost(): ApiResponse<String>

    @POST("posts/{post_id}/like")
    suspend fun likePost(): ApiResponse<String>

    @DELETE("posts/{post_id}/like")
    suspend fun unlikePost(): ApiResponse<String>

    @POST("posts/{post_id}/comments")
    suspend fun addComment(): ApiResponse<String>

    @PUT("posts/{post_id}/comments/{comment_id}")
    suspend fun updateComment(): ApiResponse<String>

    @DELETE("posts/{post_id}/comments/{comment_id}")
    suspend fun deleteComment(): ApiResponse<String>

    @POST("posts/{post_id}/comments/{comment_id}/report")
    suspend fun reportComment(): ApiResponse<String>

    @GET("posts/{post_id}/comments/{comment_id}/replies")
    suspend fun getReply(): ApiResponse<String>

    @POST("posts/{post_id}/comments/{comment_id}/replies")
    suspend fun addReply(): ApiResponse<String>

    @PUT("posts/{post_id}/comments/{comment_id}/replies/{reply_id}")
    suspend fun updateReply(): ApiResponse<String>

    @DELETE("posts/{post_id}/comments/{comment_id}/replies/{reply_id}")
    suspend fun deleteReply(): ApiResponse<String>

    @POST("posts/{post_id}/comments/{comment_id}/replies/{reply_id}/report")
    suspend fun reportReply(): ApiResponse<String>
}