package com.example.tryanimation.try_architecture_code

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TryArchitectureViewModel : ViewModel() {

    var currentNumber = MutableLiveData<Int>()
    var currentBoolean = MutableLiveData<Boolean>()

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
}