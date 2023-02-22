package com.example.tryanimation.try_architecture_code.ui.list_user

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tryanimation.try_architecture_code.database.MyDatabaseTry
import com.example.tryanimation.try_architecture_code.database.user.UserEntity
import com.example.tryanimation.try_architecture_code.database.user.UserRepository

class ListUserViewModel(context: Context) : ViewModel() {

    var listUser: LiveData<List<UserEntity>>? = null
    private var userRepository: UserRepository? = null

    init {
        val userDao = MyDatabaseTry.getMyDatabaseTry(context).userDao()
        userRepository = UserRepository(userDao)
        getListUser()
    }

    fun getListUser() {
        listUser = userRepository?.readAllUser
    }
}