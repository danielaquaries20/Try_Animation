package com.example.tryanimation.ui.kl_basic.chapter_12.data.test_data.room_database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TryFriendDao {

    @Insert
    suspend fun insert(friend: TryFriendEntity)

    @Query("SELECT * FROM TryFriendEntity")
    fun getAll(): Flow<List<TryFriendEntity>>

    @Update
    suspend fun update(friend: TryFriendEntity)

    @Delete
    suspend fun delete(friend: TryFriendEntity)

}