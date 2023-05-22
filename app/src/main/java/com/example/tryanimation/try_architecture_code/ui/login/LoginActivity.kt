package com.example.tryanimation.try_architecture_code.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.data.CoreSession
import com.crocodic.core.extension.base64encrypt
import com.crocodic.core.extension.snacked
import com.crocodic.core.helper.DateTimeHelper
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityLoginBinding
import com.example.tryanimation.try_architecture_code.api.ApiService
import com.example.tryanimation.try_architecture_code.data.const.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : CoreActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {

    @Inject
    lateinit var session: CoreSession

    @Inject
    lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnTry.setOnClickListener(this)

        observe()
//        login("leo@gmail.com", "123456")
        initToken()
    }

    private fun initToken() {
        lifecycleScope.launch {
            loadingDialog.show("Get Instance, wait for second..")
            val dateNow = DateTimeHelper().dateNow()
            val tokenInit = "$dateNow|rahasia"
            val tokenEncrypt = tokenInit.base64encrypt()
            Timber.tag("GetInstanceToken").d("1_Token: $tokenEncrypt")
            session.setValue(Constants.TOKEN.API_TOKEN, tokenEncrypt)
//            Timber.tag("GetInstanceToken").d("ApiService: $apiService")
            viewModel.getToken()
        }
    }

    private fun login(email: String?, password: String?) {
        Timber.tag("TesLogin").d("Test 2")
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            Timber.tag("TesLogin").d("Test 3")
            binding.root.snacked("Isi Email atau Password")
        } else {
            Timber.tag("TesLogin").d("Test 4")
            viewModel.login(email, password)
        }
    }

    private fun observe() {
        viewModel.loginResponse.observe(this) { response ->
            when (response.status) {
                ApiStatus.SUCCESS -> {
                    loadingDialog.dismiss()
                    binding.root.snacked("Pesan: ${response.message}")
                }
                ApiStatus.LOADING -> {
                    loadingDialog.show("Loading...")
                }
                ApiStatus.ERROR -> {
                    loadingDialog.dismiss()
                    binding.root.snacked("Error")
                }
                else -> {
                    loadingDialog.dismiss()
                    binding.root.snacked("Else Branch")
                }
            }
        }

        viewModel.tokenResponse.observe(this) { response ->
            loadingDialog.dismiss()
            when (response.status) {
                ApiStatus.SUCCESS -> {
                    binding.btnTry.isVisible = true
                }
                ApiStatus.LOADING -> {
                    loadingDialog.show("Get Token...")
                }
                ApiStatus.ERROR -> {
                    binding.root.snacked("Error")
                }
                else -> {
                    binding.root.snacked("Else Branch")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            binding.btnTry -> {
                Timber.tag("TesLogin").d("Test 1")
                login("leo@gmail.com", "123456")
            }
        }
    }


}