package com.example.tryanimation.try_architecture_code.api

import com.example.tryanimation.try_architecture_code.model.Post
import retrofit2.Response

class ApiDummyRepository {

    suspend fun getFinalPost(): Response<Post> {
        return ApiDummyInstance.apiDummy.getFinalPost()
    }
}