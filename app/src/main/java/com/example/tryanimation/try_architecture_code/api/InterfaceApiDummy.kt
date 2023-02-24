package com.example.tryanimation.try_architecture_code.api

import com.example.tryanimation.try_architecture_code.model.Post
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface InterfaceApiDummy {

    @GET("posts/1")
    suspend fun getFinalPost(): String
}