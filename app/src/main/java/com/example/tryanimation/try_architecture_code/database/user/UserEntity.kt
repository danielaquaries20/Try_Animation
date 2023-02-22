package com.example.tryanimation.try_architecture_code.database.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val firstName: String?,

    val lastName: String?,

    val age: Int?,

    val bio: String?,
)