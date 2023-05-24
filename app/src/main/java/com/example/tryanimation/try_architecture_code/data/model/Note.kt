package com.example.tryanimation.try_architecture_code.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Note(
    @Expose
    @SerializedName("id")
    val id: String,
    @Expose
    @SerializedName("title")
    val title: String?,
    @Expose
    @SerializedName("content")
    val content: String?,
    @Expose
    @SerializedName("created_at")
    val createdAt: Long?,
    @Expose
    @SerializedName("updated_at")
    val updatedAt: Long?
)
