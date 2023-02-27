package com.example.tryanimation.try_architecture_code.ui.list_post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tryanimation.try_architecture_code.api.ApiDummyRepository

class ListPostViewModelFactory(private val repositoryApiDummy: ApiDummyRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListPostViewModel(repositoryApiDummy) as T

    }
}
