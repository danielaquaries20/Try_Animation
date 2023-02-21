package com.example.tryanimation.try_architecture_code

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tryanimation.try_architecture_code.api.ApiDummyRepository

class TryArchitectureViewModelFactory(private val repositoryApiDummy: ApiDummyRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TryArchitectureViewModel(repositoryApiDummy) as T
    }
}