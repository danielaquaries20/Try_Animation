package com.example.tryanimation.try_architecture_code.ui.profile

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.extension.snacked
import com.crocodic.core.extension.text
import com.crocodic.core.extension.textOf
import com.crocodic.core.helper.StringHelper
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityEditProfileBinding
import com.example.tryanimation.try_architecture_code.database.user.UserEntity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity :
    CoreActivity<ActivityEditProfileBinding, EditProfileViewModel>(R.layout.activity_edit_profile) {

    private var myUser: UserEntity? = null
//    private var myUser: User2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe()

        binding.ivBack.setOnClickListener(this)
        binding.ivPhotoProfile.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
    }

    private fun observe() {
        viewModel.users.observe(this) { user ->
//            myUser = user
            if (user != null) {
                binding.etUsername.text(user.name)
                val avatar = StringHelper.generateTextDrawable(
                    StringHelper.getInitial(user.name?.trim()),
                    ContextCompat.getColor(this, R.color.teal_200),
                    binding.ivPhotoProfile.measuredWidth
                )
                if (user.photo.isNullOrEmpty()) {
                    binding.ivPhotoProfile.setImageDrawable(avatar)
                } else {
                    val requestOption = RequestOptions().placeholder(avatar).circleCrop()
                    Glide
                        .with(this)
                        .load(StringHelper.validateEmpty(user.photo))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(requestOption)
                        .error(avatar)
                        .into(binding.ivPhotoProfile)
                }
            } else {
                binding.etUsername.text("Username")
            }
        }

        viewModel.updateProfileResponse.observe(this) { response ->
            when (response.status) {
                ApiStatus.SUCCESS -> {
                    binding.root.snacked("Berhasil Update Profile")
                    loadingDialog.dismiss()
                    finish()
                }
                ApiStatus.LOADING -> {
                    loadingDialog.show("Loading...")
                }
                else -> {
                    loadingDialog.dismiss()
                    binding.root.snacked("Ada masalah saat Update Profile, silahkan coba lagi")
                }
            }
        }



        viewModel.user.observe(this) { user ->
            myUser = user
            if (user != null) {
                val username = user.firstName.toString()
                binding.etUsername.setText(username)
                val avatar = StringHelper.generateTextDrawable(
                    StringHelper.getInitial(username.trim()),
                    ContextCompat.getColor(this, R.color.teal_200),
                    binding.ivPhotoProfile.measuredWidth
                )
                binding.ivPhotoProfile.setImageDrawable(avatar)

                if (user.lastName.isNullOrEmpty()) {
                    binding.ivPhotoProfile.setImageDrawable(avatar)
                } else {
                    val requestOption = RequestOptions().placeholder(avatar).circleCrop()
                    Glide
                        .with(this)
                        .load(StringHelper.validateEmpty(user.lastName))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(requestOption)
                        .error(avatar)
                        .into(binding.ivPhotoProfile)
                }

            }
        }


    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            binding.ivBack -> onBackPressed()
            binding.btnSave -> validateForm()
        }
    }

    private fun validateForm() {
        val username = binding.etUsername.textOf()
        val oldName = myUser?.firstName ?: ""

        if (username.isEmpty()) {
            binding.root.snacked("Usernma tidak boleh kosong")
            return
        }
        if (username == oldName) {
            binding.root.snacked("Tidak ada perubahan data")
            return
        }
        viewModel.updateProfileName(username)
    }
}