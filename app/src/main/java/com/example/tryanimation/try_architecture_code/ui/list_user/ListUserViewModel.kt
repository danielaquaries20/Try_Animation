package com.example.tryanimation.try_architecture_code.ui.list_user

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tryanimation.try_architecture_code.database.MyDatabaseTry
import com.example.tryanimation.try_architecture_code.database.user.UserEntity
import com.example.tryanimation.try_architecture_code.database.user.UserRepository
import kotlinx.coroutines.launch

class ListUserViewModel(context: Context) : ViewModel() {

    var listUser: LiveData<List<UserEntity>>? = null
    private var userRepository: UserRepository? = null

    var response = MutableLiveData<Int>()

    init {
        val userDao = MyDatabaseTry.getMyDatabaseTry(context).userDao()
        userRepository = UserRepository(userDao)
        getListUser()
    }

    fun getListUser() {
        listUser = userRepository?.readAllUser
    }

    fun deleteAllUser() {
        viewModelScope.launch {
            try {
                userRepository?.deleteAllUser()
                response.postValue(1)
            } catch (e: Exception) {
                e.printStackTrace()
                response.postValue(0)
            }
        }
    }
}