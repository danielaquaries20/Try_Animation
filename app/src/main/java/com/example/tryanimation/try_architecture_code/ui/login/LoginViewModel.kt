package com.example.tryanimation.try_architecture_code.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.viewmodel.CoreViewModel
import com.crocodic.core.data.CoreSession
import com.example.tryanimation.try_architecture_code.api.ApiService
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val session: CoreSession,
    private val apiService: ApiService,
    private val gson: Gson
) : CoreViewModel() {

    private val _loginResponse = MutableLiveData<ApiResponse>()
    val loginResponse = _loginResponse

    override fun apiLogout() {
        TODO("Not yet implemented")
    }

    override fun apiRenewToken() {
        TODO("Not yet implemented")
    }

    fun login(email: String, password: String) {
        _loginResponse.postValue(ApiResponse(ApiStatus.LOADING, "Loading..."))
        viewModelScope.launch {
            ApiObserver(
                block = { apiService.login(email, password) },
                toast = false,
                responseListener = object : ApiObserver.ResponseListener {
                    override suspend fun onSuccess(response: JSONObject) {
                        val apiMessage = response.getString("message")
                        val token = response.getString("token")
                        session.setValue("token", token)
                        _loginResponse.postValue(ApiResponse(ApiStatus.SUCCESS, apiMessage))
                    }

                    override suspend fun onError(response: ApiResponse) {
                        //super.onError(response)
                        val responApi = response.rawResponse
                        val jsonObject = JSONObject(responApi.toString())
                        val message = jsonObject.getString("message")
                        _loginResponse.postValue(ApiResponse(ApiStatus.ERROR, message))

                    }

                }
            )
        }
    }
}