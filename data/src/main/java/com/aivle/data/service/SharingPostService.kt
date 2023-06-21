package com.aivle.data.service

import com.aivle.domain.model.sharingPost.SharingPost
import com.aivle.domain.model.sharingPost.SharingPostItem
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface SharingPostService {

    @POST("posts-create1")
    fun createPosts1(): ApiResponse<SharingPost>

    @POST("posts-create2")
    fun createPosts2(): ApiResponse<SharingPost>

    @GET("posts")
    fun getPosts(): ApiResponse<List<SharingPostItem>>

    @GET("posts/{post_id}")
    fun getPost(): ApiResponse<SharingPost>

    @PUT("posts/{post_id}")
    fun updatePost(): ApiResponse<String>

    @DELETE("posts/{post_id}")
    fun deletePost(): ApiResponse<String>

    @POST("posts/{post_id}/report")
    fun reportPost(): ApiResponse<String>

    @POST("posts/{post_id}/like")
    fun likePost(): ApiResponse<String>

    @DELETE("posts/{post_id}/like")
    fun unlikePost(): ApiResponse<String>

    @POST("posts/{post_id}/comments")
    fun addComment(): ApiResponse<String>

    @PUT("posts/{post_id}/comments/{comment_id}")
    fun updateComment(): ApiResponse<String>

    @DELETE("posts/{post_id}/comments/{comment_id}")
    fun deleteComment(): ApiResponse<String>

    @POST("posts/{post_id}/comments/{comment_id}/report")
    fun reportComment(): ApiResponse<String>

    @GET("posts/{post_id}/comments/{comment_id}/replies")
    fun getReply(): ApiResponse<String>

    @POST("posts/{post_id}/comments/{comment_id}/replies")
    fun addReply(): ApiResponse<String>

    @PUT("posts/{post_id}/comments/{comment_id}/replies/{reply_id}")
    fun updateReply(): ApiResponse<String>

    @DELETE("posts/{post_id}/comments/{comment_id}/replies/{reply_id}")
    fun deleteReply(): ApiResponse<String>

    @POST("posts/{post_id}/comments/{comment_id}/replies/{reply_id}/report")
    fun reportReply(): ApiResponse<String>
}