package com.example.tryanimation.try_architecture_code.ui.note.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.viewmodel.CoreViewModel
import com.example.tryanimation.try_architecture_code.api.ApiService
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel  @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson,
) : CoreViewModel() {
    override fun apiLogout() {
        TODO("Not yet implemented")
    }

    override fun apiRenewToken() {
        TODO("Not yet implemented")
    }

    private val _editNoteResponse = MutableLiveData<ApiResponse>()
    val editNoteResponse = _editNoteResponse

    private val _deleteNoteResponse = MutableLiveData<ApiResponse>()
    val deleteNoteResponse = _deleteNoteResponse

    fun editNote(idNote: String, title: String, content: String) {
        viewModelScope.launch {
            _editNoteResponse.postValue(ApiResponse(ApiStatus.LOADING, "Loading..."))
            ApiObserver(
                block = { apiService.updateNote(idNote, title, content) },
                toast = false,
                responseListener = object : ApiObserver.ResponseListener {
                    override suspend fun onSuccess(response: JSONObject) {
                        val apiMessage = response.getString("message")
                        _editNoteResponse.postValue(ApiResponse(ApiStatus.SUCCESS, apiMessage))
                    }
                    override suspend fun onError(response: ApiResponse) {
                        super.onError(response)
                        _editNoteResponse.postValue(ApiResponse(ApiStatus.ERROR))
                    }
                }
            )
        }
    }

    fun deleteNote(idNote: String) {
        viewModelScope.launch {
            _deleteNoteResponse.postValue(ApiResponse(ApiStatus.LOADING, "Loading..."))
            ApiObserver(
                block = { apiService.deleteNote(idNote) },
                toast = false,
                responseListener = object : ApiObserver.ResponseListener {
                    override suspend fun onSuccess(response: JSONObject) {
                        val apiMessage = response.getString("message")
                        _deleteNoteResponse.postValue(ApiResponse(ApiStatus.SUCCESS, apiMessage))
                    }
                    override suspend fun onError(response: ApiResponse) {
                        super.onError(response)
                        _deleteNoteResponse.postValue(ApiResponse(ApiStatus.ERROR))
                    }
                }
            )
        }
    }


}