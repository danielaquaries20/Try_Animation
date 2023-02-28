package com.example.tryanimation.try_architecture_code.api

import android.util.Log
import com.example.tryanimation.try_architecture_code.model.Post
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class ApiDummyRepository {

    private val tagApi = "API"
    private val gson: Gson =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    suspend fun getFinalPost(): Post {
        return try {
            val response = ApiDummyInstance.apiDummy.getFinalPost()
            if (response.isSuccessful) {
                val responseJSON = withContext(Dispatchers.IO) {
                    JSONObject(response.body().toString())
                }
                Log.d(tagApi, "ResponseSuccess: $responseJSON")
                gson.fromJson(responseJSON.toString(), Post::class.java)
            } else {
                Post(id = 0, title = "Error", content = response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(tagApi, "ResponseError: $e")
            Post(id = 0, title = "Error", content = e.toString())
        }
    }

    suspend fun getSpecificPost(myHeader: String, postId: String): Post {
        return try {
            val response = ApiDummyInstance.apiDummy.getSpecificPost(myHeader, "True", postId)
            if (response.isSuccessful) {
                val responseJSON = withContext(Dispatchers.IO) {
                    JSONObject(response.body().toString())
                }
                Log.d(tagApi, "ResponseSuccess: $responseJSON")
                gson.fromJson(responseJSON.toString(), Post::class.java)
            } else {
                Post(id = 0, title = "Error", content = response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(tagApi, "ResponseError: $e")
            Post(id = 0, title = "Error", content = e.toString())
        }
    }

    suspend fun getListPostByUserId(userId: String): List<Post> {
        val emitList = ArrayList<Post>()
        try {
            val response = ApiDummyInstance.apiDummy.getListPostUserId(userId)
            return if (response.isSuccessful) {
                val responseJSON = withContext(Dispatchers.IO) {
                    JSONArray(response.body().toString())
                }
                Log.d(tagApi, "ResponseSuccess: $responseJSON")
                emitList.clear()
                emitList.addAll(gson.fromJson(responseJSON.toString(), Array<Post>::class.java)
                    .toList())
                emitList
            } else {
                emitList.clear()
                emitList.add(Post(id = 0, title = "Error", content = response.message()))
                emitList
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(tagApi, "ResponseError: $e")
            emitList.clear()
            emitList.add(Post(id = 0, title = "Error", content = e.toString()))
            return emitList
        }
    }

    suspend fun createPost(userId: Int, id: Int, title: String, body: String): Post {
        return try {
            val response = ApiDummyInstance.apiDummy.createPost(userId, id, title, body)
            if (response.isSuccessful) {
                val responseJSON = withContext(Dispatchers.IO) {
                    JSONObject(response.body().toString())
                }
                Log.d(tagApi, "ResponseSuccess: $responseJSON")
                gson.fromJson(responseJSON.toString(), Post::class.java)

            } else {
                Post(id = 0, title = "Error", content = response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(tagApi, "ResponseError: $e")
            Post(id = 0, title = "Error", content = e.toString())
        }
    }

    suspend fun createPostByJson(post: Post): Post {
        return try {
            val response = ApiDummyInstance.apiDummy.createPostByJson(post)
            if (response.isSuccessful) {
                val responseJSON = withContext(Dispatchers.IO) {
                    JSONObject(response.body().toString())
                }
                Log.d(tagApi, "ResponseSuccess: $responseJSON")
                gson.fromJson(responseJSON.toString(), Post::class.java)

            } else {
                Post(id = 0, title = "Error", content = response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(tagApi, "ResponseError: $e")
            Post(id = 0, title = "Error", content = e.toString())
        }
    }


}