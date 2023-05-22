package com.example.tryanimation.try_architecture_code.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.viewmodel.CoreViewModel
import com.crocodic.core.data.CoreSession
import com.example.tryanimation.try_architecture_code.api.ApiService
import com.example.tryanimation.try_architecture_code.data.const.Constants
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
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

    private val _tokenResponse = MutableLiveData<ApiResponse>()
    val tokenResponse = _tokenResponse

    override fun apiLogout() {
        TODO("Not yet implemented")
    }

    override fun apiRenewToken() {
        TODO("Not yet implemented")
    }

    fun getToken() {
        try {
            viewModelScope.launch {
                Timber.tag("ErrorAPI").e("Error 1")
                _tokenResponse.postValue(ApiResponse(ApiStatus.LOADING, "Loading..."))
                Timber.tag("ErrorAPI").e("Error 2")
                ApiObserver(
                    block = { apiService.getToken() },
                    toast = false,
                    responseListener = object : ApiObserver.ResponseListener {
                        override suspend fun onSuccess(response: JSONObject) {
                            Timber.tag("ErrorAPI").e("Error 3")
                            val token = response.getString("token")
                            session.setValue(Constants.TOKEN.API_TOKEN, token)
                            Timber.tag("GetInstanceToken").d("2_Token: $token")
                            _tokenResponse.postValue(ApiResponse(ApiStatus.SUCCESS))
                            Timber.tag("ErrorAPI").e("Error 4")
                        }

                        override suspend fun onError(response: ApiResponse) {
                            Timber.tag("ErrorAPI").e("Error 5")
                            super.onError(response)
                        }
                    }
                )
            }
        } catch (e:Exception) {
            e.printStackTrace()
        }
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
                        Timber.tag("GetInstanceToken").d("3_Token: $token")
                        session.setValue(Constants.TOKEN.API_TOKEN, token)
                        _loginResponse.postValue(ApiResponse(ApiStatus.SUCCESS, apiMessage))
                    }

                    override suspend fun onError(response: ApiResponse) {
                        super.onError(response)

                    }

                }
            )
        }
    }

}