package com.example.tryanimation.try_architecture_code.database.user

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    val readAllUser: LiveData<List<UserEntity>> = userDao.readAllUser()

    suspend fun addUser(user: UserEntity) {
        userDao.addUser(user)
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }


    suspend fun deleteUser(user: UserEntity) {
        userDao.deleteUser(user)
    }

    suspend fun deleteAllUser() {
        userDao.deleteAllUser()
    }
}