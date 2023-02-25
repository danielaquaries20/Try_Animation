package com.example.tryanimation.try_architecture_code.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InterfaceApiDummy {

    @GET("posts/1")
    suspend fun getFinalPost(): Response<String>

    @GET("posts/{postId}")
    suspend fun getSpecificPost(
        @Path("postId") id: String,
    ): Response<String>

    @GET("posts")
    suspend fun getListPostUserId(
        @Query("userId") userId: String
    ): Response<String>


}