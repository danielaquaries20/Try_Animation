package com.example.tryanimation.try_architecture_code.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.viewmodel.CoreViewModel
import com.crocodic.core.data.CoreSession
import com.crocodic.core.extension.toObject
import com.example.tryanimation.try_architecture_code.api.ApiService
import com.example.tryanimation.try_architecture_code.data.const.Constants
import com.example.tryanimation.try_architecture_code.database.user.UserDao
import com.example.tryanimation.try_architecture_code.database.user.UserEntity
import com.example.tryanimation.try_architecture_code.model.User2
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val session: CoreSession,
    private val apiService: ApiService,
    private val userDao: UserDao,
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
            viewModelScope.launch {
                _tokenResponse.postValue(ApiResponse(ApiStatus.LOADING, "Loading..."))
                ApiObserver(
                    block = { apiService.getToken() },
                    toast = false,
                    responseListener = object : ApiObserver.ResponseListener {
                        override suspend fun onSuccess(response: JSONObject) {
                            val token = response.getString("token")
                            session.setValue(Constants.TOKEN.API_TOKEN, token)
                            _tokenResponse.postValue(ApiResponse(ApiStatus.SUCCESS))
                        }

                        override suspend fun onError(response: ApiResponse) {
                            super.onError(response)
                        }
                    }
                )
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

                        //Dibagian ini di cek dan di samakan
                        val apiMessage = response.getString("message")
                        val token = response.getString("token")
                        val data = response.getJSONObject("data").toObject<User2>(gson)
                        val user = UserEntity(1, data.name, data.photo, null, data.id)
                        userDao.addUser(user)

                        session.setValue(Constants.TOKEN.API_TOKEN, token)
                        Timber.tag("GetInstanceToken").d("RealToken: $token")
                        _loginResponse.postValue(ApiResponse(ApiStatus.SUCCESS, apiMessage))
                    }

                    override suspend fun onError(response: ApiResponse) {

                        //yang sebelumnya dihapus, diganti dengan ini
                        super.onError(response)

                    }

                }
            )
        }
    }

    fun checkLogin(isLogin: (Boolean) -> Unit) {
        viewModelScope.launch {
            var login = false
            CoroutineScope(Dispatchers.IO).launch {
                login = userDao.isLogin()
            }
            delay(1000)
            isLogin(login)
        }
    }
}