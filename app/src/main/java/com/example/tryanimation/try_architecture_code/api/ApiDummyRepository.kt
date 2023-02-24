package com.example.tryanimation.try_architecture_code.api

import android.util.Log
import com.example.tryanimation.try_architecture_code.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ApiDummyRepository {

    suspend fun getFinalPost(): Post {
        try {
            val responseJSON = withContext(Dispatchers.IO) {
                val response = ApiDummyInstance.apiDummy.getFinalPost()
                JSONObject(response)
            }
            Log.d("GetPost", "ResponseSuccess: $responseJSON")
//                val jsonResponse = JSONObject(response.body().toString())
            return Post(id = 1, title = "Data", content = "ResponseJSON: $responseJSON")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("GetPost", "ResponseError: $e")
            return Post(id = 0, title = "Error", content = e.toString())
        }
    }
}