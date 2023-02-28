package com.example.tryanimation.try_architecture_code.api

import com.example.tryanimation.try_architecture_code.model.Post
import retrofit2.Response
import retrofit2.http.*

interface InterfaceApiDummy {

    @GET("posts/1")
    suspend fun getFinalPost(): Response<String>

    @GET("posts/{postId}")
    suspend fun getSpecificPost(
        @Path("postId") id: String,
    ): Response<String>

    @GET("posts")
    suspend fun getListPostUserId(
        @Query("userId") userId: String,
    ): Response<String>

    @FormUrlEncoded
    @POST("posts")
    suspend fun createPost(
        @Field("userId") userId: Int,
        @Field("id") id: Int,
        @Field("title") title: String,
        @Field("body") body: String,
    ): Response<String>

    @POST("posts")
    suspend fun createPostByJson(
        @Body post: Post,
    ): Response<String>

}