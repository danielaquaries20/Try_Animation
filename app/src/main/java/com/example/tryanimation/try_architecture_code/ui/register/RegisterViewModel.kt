package com.example.tryanimation.try_architecture_code.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.viewmodel.CoreViewModel
import com.crocodic.core.data.CoreSession
import com.example.tryanimation.try_architecture_code.api.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val session: CoreSession,
    private val apiService: ApiService,
) : CoreViewModel() {

    override fun apiLogout() {
        TODO("Not yet implemented")
    }

    override fun apiRenewToken() {
        TODO("Not yet implemented")
    }

    private val _registerResponse = MutableLiveData<ApiResponse>()
    val registerResponse = _registerResponse
    //Menambahkan fungsi Register
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerResponse.postValue(ApiResponse(ApiStatus.LOADING, "Loading..."))
            ApiObserver(
                block = { apiService.register(name, email, password) },
                toast = false,
                responseListener = object : ApiObserver.ResponseListener {
                    override suspend fun onSuccess(response: JSONObject) {
                        val apiMessage = response.getString("message")
                        _registerResponse.postValue(ApiResponse(ApiStatus.SUCCESS, apiMessage))
                    }
                    override suspend fun onError(response: ApiResponse) {
                        super.onError(response)
                    }
                }
            )
        }
    }

}