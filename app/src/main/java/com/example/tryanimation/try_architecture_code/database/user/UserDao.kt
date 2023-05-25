package com.example.tryanimation.try_architecture_code.database.user

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllUser(): LiveData<List<UserEntity>>

    @Delete()
    suspend fun deleteUser(user: UserEntity)

    @Query("DELETE FROM user_table")
    suspend fun deleteAllUser()

    @Query("SELECT * from user_table WHERE id = 1 LIMIT 1")
    fun getUser(): LiveData<UserEntity?>
}