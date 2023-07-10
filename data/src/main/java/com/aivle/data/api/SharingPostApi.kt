package com.aivle.data.api

import com.aivle.data.entity.sharingPost.CommentEntity
import com.aivle.data.entity.sharingPost.ReplyEntity
import com.aivle.data.entity.sharingPost.SharingPostCreateEntity
import com.aivle.data.entity.sharingPost.SharingPostDetailEntity
import com.aivle.data.entity.sharingPost.SharingPostItemEntity
import com.skydoves.sandwich.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SharingPostApi {

    @POST("posts/create")
    suspend fun createPost(
        @Body post: SharingPostCreateEntity
    ): ApiResponse<SharingPostItemEntity>

    @GET("posts")
    suspend fun getPosts(
        @Query("address_district") district: String
    ): ApiResponse<List<SharingPostItemEntity>>

    @GET("posts/{post_id}")
    suspend fun getPostDetail(
        @Path("post_id") post_id: Int
    ): ApiResponse<SharingPostDetailEntity>

    @PUT("posts/{post_id}")
    suspend fun updatePost(
        @Path("post_id") post_id: Int,
        @Body post: SharingPostDetailEntity,
    ): ApiResponse<Unit>

    @DELETE("posts/{post_id}")
    suspend fun deletePost(
        @Path("post_id") post_id: Int
    ): Call<Unit> // 204 No Content

    @PUT("posts/{post_id}/sharing")
    suspend fun completeSharingPost(
        @Path("post_id") post_id: Int
    ): ApiResponse<Map<String, Int>>

    @POST("posts/{post_id}/report")
    suspend fun reportPost(
        @Path("post_id") post_id: Int
    ): ApiResponse<Map<String, Int>>

    @POST("posts/{post_id}/like")
    suspend fun likePost(
        @Path("post_id") post_id: Int
    ): ApiResponse<Map<String, Int>>

    @DELETE("posts/{post_id}/like")
    suspend fun unlikePost(
        @Path("post_id") post_id: Int
    ): ApiResponse<Map<String, String>>

    @POST("posts/{post_id}/comments")
    suspend fun addComment(
        @Path("post_id") post_id: Int,
        @Body comment: Map<String, String>, // "comment":"test"
    ): ApiResponse<CommentEntity>

    @PUT("posts/{post_id}/comments/{comment_id}")
    suspend fun updateComment(
        @Path("post_id") post_id: Int,
        @Path("comment_id") comment_id: Int,
        @Body comment: Map<String, String>, // "comment":"test"
    ): ApiResponse<CommentEntity>

    @DELETE("posts/{post_id}/comments/{comment_id}")
    suspend fun deleteComment(
        @Path("post_id") post_id: Int,
        @Path("comment_id") comment_id: Int,
    ): ApiResponse<Unit>

    @POST("posts/{post_id}/comments/{comment_id}/report")
    suspend fun reportComment(
        @Path("post_id") post_id: Int,
        @Path("comment_id") comment_id: Int,
    ): ApiResponse<Map<String, Int>>

    @GET("posts/{post_id}/comments/{comment_id}/replies")
    suspend fun getReplies(
        @Path("post_id") post_id: Int,
        @Path("comment_id") comment_id: Int,
    ): ApiResponse<List<ReplyEntity>>

    @POST("posts/{post_id}/comments/{comment_id}/replies")
    suspend fun addReply(
        @Path("post_id") post_id: Int,
        @Path("comment_id") comment_id: Int,
        @Body reply: Map<String, String>, // "reply": "test"
    ): ApiResponse<ReplyEntity>

    @PUT("posts/{post_id}/comments/{comment_id}/replies/{reply_id}")
    suspend fun updateReply(
        @Path("post_id") post_id: Int,
        @Path("comment_id") comment_id: Int,
        @Path("reply_id") reply_id: Int,
        @Body reply: Map<String, String> // "reply": "수정된 대댓글 내용~~~"
    ): ApiResponse<ReplyEntity>

    @DELETE("posts/{post_id}/comments/{comment_id}/replies/{reply_id}")
    suspend fun deleteReply(
        @Path("post_id") post_id: Int,
        @Path("comment_id") comment_id: Int,
        @Path("reply_id") reply_id: Int,
    ): Call<Unit> // 204 No Content

    @POST("posts/{post_id}/comments/{comment_id}/replies/{reply_id}/report")
    suspend fun reportReply(
        @Path("post_id") post_id: Int,
        @Path("comment_id") comment_id: Int,
        @Path("reply_id") reply_id: Int,
    ): ApiResponse<Map<String, Int>>
}