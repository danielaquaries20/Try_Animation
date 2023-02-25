package com.example.tryanimation.try_architecture_code.api

import android.util.Log
import com.example.tryanimation.try_architecture_code.model.Post
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ApiDummyRepository {

    private val gson: Gson =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    suspend fun getFinalPost(): Post {
        return try {
            val response = ApiDummyInstance.apiDummy.getFinalPost()
            if (response.isSuccessful) {
                val responseJSON = withContext(Dispatchers.IO) {
                    JSONObject(response.body().toString())
                }
                Log.d("GetPost", "ResponseSuccess: $responseJSON")
                gson.fromJson(responseJSON.toString(), Post::class.java)
            } else {
                Post(id = 0, title = "Error", content = response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("GetPost", "ResponseError: $e")
            Post(id = 0, title = "Error", content = e.toString())
        }
    }
}