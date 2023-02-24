package com.example.tryanimation.try_architecture_code.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("userId")
    val userId: Int? = null,

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("body")
    val content: String? = null,
)
