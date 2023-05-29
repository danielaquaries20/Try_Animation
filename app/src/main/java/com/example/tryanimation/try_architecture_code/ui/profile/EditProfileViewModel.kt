package com.example.tryanimation.try_architecture_code.ui.profile

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
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val gson: Gson
) : CoreViewModel() {
    override fun apiLogout() {
        TODO("Not yet implemented")
    }

    override fun apiRenewToken() {
        TODO("Not yet implemented")
    }

    val user = userDao.getUser()

    private val _updateProfileResponse = MutableLiveData<ApiResponse>()
    val updateProfileResponse = _updateProfileResponse

    fun updateProfileName(name: String) {
        _updateProfileResponse.postValue(ApiResponse(ApiStatus.LOADING, "Loading..."))
        viewModelScope.launch {
            ApiObserver(
                block = { apiService.updateNameProfile(name) },
                toast = false,
                responseListener = object : ApiObserver.ResponseListener {
                    override suspend fun onSuccess(response: JSONObject) {

                        val apiMessage = response.getString("message")
                        val data = response.getJSONObject("data").toObject<User2>(gson)
                        val user = UserEntity(1, data.name, data.photo, null, data.id)
                        userDao.updateUser(user)

                        _updateProfileResponse.postValue(ApiResponse(ApiStatus.SUCCESS, apiMessage))
                    }

                    override suspend fun onError(response: ApiResponse) {
                        super.onError(response)
                    }

                }
            )
        }
    }

    fun updateProfile(name: String, photoProfile: File) {
        val fileBody = photoProfile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("photo", photoProfile.name, fileBody)
        _updateProfileResponse.postValue(ApiResponse(ApiStatus.LOADING, "Loading..."))
        viewModelScope.launch {
            ApiObserver(
                block = { apiService.updateProfile(name, filePart) },
                toast = false,
                responseListener = object : ApiObserver.ResponseListener {
                    override suspend fun onSuccess(response: JSONObject) {

                        val apiMessage = response.getString("message")
                        val data = response.getJSONObject("data").toObject<User2>(gson)
                        val user = UserEntity(1, data.name, data.photo, null, data.id)
                        userDao.updateUser(user)

                        _updateProfileResponse.postValue(ApiResponse(ApiStatus.SUCCESS, apiMessage))
                    }

                    override suspend fun onError(response: ApiResponse) {
                        super.onError(response)
                    }

                }
            )
        }
    }













    val users = MutableLiveData<User2>()

}