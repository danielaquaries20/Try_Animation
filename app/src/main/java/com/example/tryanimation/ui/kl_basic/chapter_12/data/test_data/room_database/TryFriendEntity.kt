package com.example.tryanimation.ui.kl_basic.chapter_12.data.test_data.room_database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TryFriendEntity(
    var name: String? = null,
    var school: String? = null,
    var hobby: String? = null,
    var photo: String? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
