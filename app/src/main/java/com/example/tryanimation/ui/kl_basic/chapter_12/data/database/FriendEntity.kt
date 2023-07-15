package com.example.tryanimation.ui.kl_basic.chapter_12.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FriendEntity(
    var name : String,
    var school : String,
    var hobby : String,
    var photo : String? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}
