package com.example.tryanimation.try_architecture_code.api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("user/get-token")
    suspend fun getToken(): String

    @FormUrlEncoded
    @POST("user/login?expired=60")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): String

    @FormUrlEncoded
    @POST("user")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): String

    @GET("note")
    suspend fun getNotes(): String
}