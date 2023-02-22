package com.example.tryanimation.try_architecture_code.ui.detail_user

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tryanimation.try_architecture_code.ui.list_user.ListUserViewModel

class DetailUserViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailUserViewModel(context) as T
    }

}