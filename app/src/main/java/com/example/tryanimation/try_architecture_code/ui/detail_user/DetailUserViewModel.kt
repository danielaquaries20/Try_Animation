package com.example.tryanimation.try_architecture_code.ui.detail_user

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tryanimation.try_architecture_code.database.MyDatabaseTry
import com.example.tryanimation.try_architecture_code.database.user.UserDao
import com.example.tryanimation.try_architecture_code.database.user.UserEntity
import com.example.tryanimation.try_architecture_code.database.user.UserRepository
import kotlinx.coroutines.launch

class DetailUserViewModel(context: Context) : ViewModel() {

    var response = MutableLiveData<Int>()
    private val getUserDetail: LiveData<UserEntity>? = null
    private var userRepository: UserRepository? = null
    private var userDao: UserDao? = null

    init {
        userDao = MyDatabaseTry.getMyDatabaseTry(context).userDao()
        userDao?.let { userRepository = UserRepository(it) }
    }

    fun addUser(firstName: String, lastName: String, age: Int?, bio: String?) {
        viewModelScope.launch {
            try {
                val user = UserEntity(0, firstName, lastName, age, bio)
                userDao?.addUser(user)
                response.postValue(1)
            } catch (e: Exception) {
                e.printStackTrace()
                response.postValue(0)
            }
        }
    }

    fun updateUser(idUser: Int, firstName: String, lastName: String, age: Int?, bio: String?) {
        viewModelScope.launch {
            try {
                val user = UserEntity(idUser, firstName, lastName, age, bio)
                userDao?.updateUser(user)
                response.postValue(2)
            } catch (e: Exception) {
                e.printStackTrace()
                response.postValue(0)
            }
        }
    }

}