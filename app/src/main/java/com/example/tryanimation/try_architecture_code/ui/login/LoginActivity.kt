package com.example.tryanimation.try_architecture_code.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.data.CoreSession
import com.crocodic.core.extension.base64encrypt
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.snacked
import com.crocodic.core.extension.textOf
import com.crocodic.core.helper.DateTimeHelper
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityLoginBinding
import com.example.tryanimation.try_architecture_code.api.ApiService
import com.example.tryanimation.try_architecture_code.data.const.Constants
import com.example.tryanimation.try_bottom_navigation.TryBottomNavigationActivity
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

        viewModel.checkLogin { isLogin ->
            Timber.d("CheckLogin: $isLogin")
            if (isLogin) {
                openActivity<TryBottomNavigationActivity>()
                finish()
            } else {
                binding.btnTry.setOnClickListener(this)

                observe()
                initToken()
            }
        }

    }

    private fun validateForm() {
        val email = binding.etUsername.textOf()
        val password = binding.etPassword.textOf()

        if (email.isEmpty()) {
            binding.root.snacked("Email tidak boleh kosong")
            return
        }

        if (password.isEmpty()) {
            binding.root.snacked("Password tidak boleh kosong")
            return
        }

        login(email, password)
    }

    private fun initToken() {
        lifecycleScope.launch {
            loadingDialog.show("Get Instance, wait for second..")
            val dateNow = DateTimeHelper().dateNow()
            val tokenInit = "$dateNow|rahasia"
            val tokenEncrypt = tokenInit.base64encrypt()
            Timber.tag("GetInstanceToken").d("EncryptToken: $tokenEncrypt")
            session.setValue(Constants.TOKEN.API_TOKEN, tokenEncrypt)
//            Timber.tag("GetInstanceToken").d("ApiService: $apiService")
            viewModel.getToken()
        }
    }

    private fun login(email: String?, password: String?) {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            binding.root.snacked("Isi Email atau Password")
        } else {
            viewModel.login(email, password)
        }
    }

    private fun observe() {

        //Login response di cek dan di sesuaikan
        viewModel.loginResponse.observe(this) { response ->
            when (response.status) {
                ApiStatus.SUCCESS -> {
                    loadingDialog.dismiss()
                    finish()
                    openActivity<TryBottomNavigationActivity>()
                }
                ApiStatus.LOADING -> {
                    //diganti ini
                    loadingDialog.show("Loading...")
                }
                else -> {
                    loadingDialog.dismiss()
                    binding.root.snacked("Else Branch")
                }
            }
        }

        // dibagian observe ditambahkan ini
        viewModel.tokenResponse.observe(this) { response ->
            loadingDialog.dismiss()
            when (response.status) {
                ApiStatus.SUCCESS -> {
                    binding.btnTry.isVisible = true
                }
                ApiStatus.LOADING -> {
                    loadingDialog.show("Get Token...")
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
            binding.btnTry -> { validateForm() }
        }
    }


}