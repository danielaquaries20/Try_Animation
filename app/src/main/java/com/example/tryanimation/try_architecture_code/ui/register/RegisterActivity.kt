package com.example.tryanimation.try_architecture_code.ui.register

import android.os.Bundle
import android.view.View
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.extension.snacked
import com.crocodic.core.extension.textOf
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity :
    CoreActivity<ActivityRegisterBinding, RegisterViewModel>(R.layout.activity_register) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnRegister.setOnClickListener(this)

        observe()
    }

    private fun validateForm() {
        val name = binding.etUsername.textOf()
        val email = binding.etEmail.textOf()
        val password = binding.etPassword.textOf()

        if (name.isEmpty()) {
            binding.root.snacked("Username tidak boleh kosong")
            return
        }

        if (email.isEmpty()) {
            binding.root.snacked("Email tidak boleh kosong")
            return
        }

        if (password.isEmpty()) {
            binding.root.snacked("Password tidak boleh kosong")
            return
        }

        viewModel.register(name, email, password)
    }

    private fun observe() {
        viewModel.registerResponse.observe(this) { response ->
            when (response.status) {
                ApiStatus.SUCCESS -> {
                    loadingDialog.dismiss()
                    finish()
                }
                ApiStatus.LOADING -> {
                    loadingDialog.show("Loading...")
                }
                else -> {
                    loadingDialog.dismiss()
                    binding.root.snacked("Error")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v) {
            binding.btnRegister -> validateForm()
        }
    }


    //Ini sudah selesai untuk register
    //Bisa di run ke hp masing masing
    //Dan
}