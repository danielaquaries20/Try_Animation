package com.example.tryanimation.try_architecture_code.ui.note.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.viewmodel.CoreViewModel
import com.example.tryanimation.try_architecture_code.api.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val apiService: ApiService,
) : CoreViewModel() {
    override fun apiLogout() {
        TODO("Not yet implemented")
    }

    override fun apiRenewToken() {
        TODO("Not yet implemented")
    }

    private val _addNoteResponse = MutableLiveData<ApiResponse>()
    val addNoteResponse = _addNoteResponse

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            _addNoteResponse.postValue(ApiResponse(ApiStatus.LOADING, "Loading..."))
            ApiObserver(
                block = { apiService.createNote(title, content) },
                toast = false,
                responseListener = object : ApiObserver.ResponseListener {
                    override suspend fun onSuccess(response: JSONObject) {
                        val apiMessage = response.getString("message")
                        _addNoteResponse.postValue(ApiResponse(ApiStatus.SUCCESS, apiMessage))
                    }
                    override suspend fun onError(response: ApiResponse) {
                        super.onError(response)
                        _addNoteResponse.postValue(ApiResponse(ApiStatus.ERROR))
                    }
                }
            )
        }
    }


}