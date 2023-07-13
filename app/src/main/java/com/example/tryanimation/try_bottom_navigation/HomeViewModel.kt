package com.example.tryanimation.try_bottom_navigation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.viewmodel.CoreViewModel
import com.crocodic.core.data.CoreSession
import com.crocodic.core.extension.toList
import com.example.tryanimation.try_architecture_code.api.ApiService
import com.example.tryanimation.try_architecture_code.data.model.Note
import com.example.tryanimation.try_architecture_code.database.MyDatabaseTry
import com.example.tryanimation.try_architecture_code.database.user.UserDao
import com.example.tryanimation.try_architecture_code.database.user.UserRepository
import com.example.tryanimation.try_architecture_code.model.User2
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson,
    private val session: CoreSession,
    userDao: UserDao,
    private val appDatabase: MyDatabaseTry
) : CoreViewModel() {
    override fun apiLogout() {
        TODO("Not yet implemented")
    }

    override fun apiRenewToken() {
        TODO("Not yet implemented")
    }

    val user = userDao.getUser()

    private val _listNote = MutableLiveData<List<Note>>()
    val listNote = _listNote

    private val _noteResponse = MutableLiveData<ApiResponse>()
    val noteResponse = _noteResponse

    fun getNotes() {
        viewModelScope.launch {
            _noteResponse.postValue(ApiResponse(ApiStatus.LOADING, "Loading..."))
            ApiObserver(
                block = { apiService.getNotes() },
                toast = false,
                responseListener = object : ApiObserver.ResponseListener {
                    override suspend fun onSuccess(response: JSONObject) {
                        val apiMessage = response.getString("message")
                        val data = response.getJSONArray("data").toList<Note>(gson)
                        Log.d("HomeViewModel", "ListNote: $data")
                        _listNote.postValue(data)
                        _noteResponse.postValue(ApiResponse(ApiStatus.SUCCESS, apiMessage))
                    }

                    override suspend fun onError(response: ApiResponse) {
                        super.onError(response)
                    }
                }
            )
        }
    }

    fun logout(isLogout: () -> Unit) {
        viewModelScope.launch {
            session.clearAll()
            CoroutineScope(Dispatchers.IO).launch {
                appDatabase.clearAllTables()
            }
            isLogout()
        }
    }













    val users = MutableLiveData<User2>()

}