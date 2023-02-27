package com.example.tryanimation.try_architecture_code.ui.list_post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tryanimation.try_architecture_code.api.ApiDummyRepository
import com.example.tryanimation.try_architecture_code.model.Post
import kotlinx.coroutines.launch

class ListPostViewModel(private val repositoryApiDummy: ApiDummyRepository) : ViewModel() {

    var listPost = MutableLiveData<List<Post>>()

    fun getListPostByUserId(userId: String) {
        viewModelScope.launch {
            val response = repositoryApiDummy.getListPostByUserId(userId)
            listPost.postValue(response)
        }
    }

}