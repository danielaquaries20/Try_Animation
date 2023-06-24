package com.example.tryanimation.ui.kl_basic.chapter_12.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendDao {

    @Insert
    suspend fun insert(friend: FriendEntity)

    @Update
    suspend fun update(friend: FriendEntity)

    @Delete
    suspend fun delete(friend: FriendEntity)

    @Query("SELECT * FROM FriendEntity")
    fun getAll() : Flow<List<FriendEntity>>

}