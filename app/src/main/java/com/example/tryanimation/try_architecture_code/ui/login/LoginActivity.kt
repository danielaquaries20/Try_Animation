package com.example.tryanimation.try_architecture_code.ui.login

import android.os.Bundle
import android.view.View
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.extension.snacked
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : CoreActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe()
//        login("leo@gmail.com", "123456")
    }

    private fun login(email: String?, password: String?) {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            binding.root.snacked("Isi Email atau Password")
        } else {
            viewModel.login(email, password)
        }
    }

    private fun observe() {
        viewModel.loginResponse.observe(this) { response ->
            when (response.status) {
                ApiStatus.SUCCESS -> {
                    binding.root.snacked("Pesan: ${response.message}")
                }
                ApiStatus.LOADING -> {
                    binding.root.snacked("${response.message}")
                }
                ApiStatus.ERROR -> {
                    binding.root.snacked("Error: ${response.message}")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v) {
            binding.btnTry -> {login("leo@gmail.com", "123456")}
        }
    }


}