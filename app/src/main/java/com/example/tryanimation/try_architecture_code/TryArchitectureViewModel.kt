package com.example.tryanimation.try_architecture_code

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tryanimation.try_architecture_code.api.ApiDummyRepository
import com.example.tryanimation.try_architecture_code.model.Post
import kotlinx.coroutines.launch
import retrofit2.Response

class TryArchitectureViewModel(private val repositoryApiDummy: ApiDummyRepository) : ViewModel() {

    var currentNumber = MutableLiveData<Int>()
    var currentBoolean = MutableLiveData<Boolean>()

    var apiResponse = MutableLiveData<Response<Post>>()

    fun increase(number: Int) {
        val newNumber = number + 1
        val newBoolean = newNumber % 2 == 0
        currentNumber.postValue(newNumber)
        currentBoolean.postValue(newBoolean)
    }

    fun decrease(number: Int) {
        val newNumber = number - 1
        val newBoolean = newNumber % 2 == 0
        currentNumber.postValue(newNumber)
        currentBoolean.postValue(newBoolean)
    }

    fun getFinalPost() {
        viewModelScope.launch {
            val response = repositoryApiDummy.getFinalPost()
            apiResponse.postValue(response)
        }
    }
}