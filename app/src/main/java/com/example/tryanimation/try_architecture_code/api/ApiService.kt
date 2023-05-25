package com.example.tryanimation.try_architecture_code.api

import retrofit2.http.*

interface ApiService {

    @GET("api/user/get-token")
    suspend fun getToken(): String

    @FormUrlEncoded
    @POST("api/user/login?expired=60")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): String

    @FormUrlEncoded
    @POST("api/user")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): String

    @GET("api/note")
    suspend fun getNotes(): String

    @FormUrlEncoded
    @POST("api/note")
    suspend fun createNote(
        @Field("title") title: String?,
        @Field("content") content: String?,
    ): String

    @FormUrlEncoded
    @PATCH("api/note/{id}")
    suspend fun updateNote(
        @Path("id") id: String,
        @Field("title") title: String?,
        @Field("content") content: String?,
    ): String

    @DELETE("api/note/{id}")
    suspend fun deleteNote(
        @Path("id") id: String
    ): String
}