package com.example.tryanimation.try_bottom_navigation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.crocodic.core.base.fragment.CoreFragment
import com.crocodic.core.extension.text
import com.crocodic.core.helper.StringHelper
import com.example.tryanimation.R
import com.example.tryanimation.databinding.FragmentProfileBinding
import com.example.tryanimation.try_architecture_code.ui.login.LoginActivity
import com.example.tryanimation.try_architecture_code.ui.profile.EditProfileActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : CoreFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private lateinit var viewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this@ProfileFragment)[HomeViewModel::class.java]

        observe()

        binding.btnEditProfile.setOnClickListener {
            val toEditProfile = Intent(requireActivity(), EditProfileActivity::class.java)
            startActivity(toEditProfile)
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun observe() {
        viewModel.user.observe(requireActivity()) { user ->
            if (user != null) {
                binding.tvName.text(user.firstName)
                val avatar = StringHelper.generateTextDrawable(
                    StringHelper.getInitial(user.firstName.toString().trim()),
                    ContextCompat.getColor(requireContext(), R.color.teal_200),
                    binding.ivPhotoProfile.measuredWidth
                )

                if (user.lastName.isNullOrEmpty()) {
                    binding.ivPhotoProfile.setImageDrawable(avatar)
                } else {
                    val requestOption = RequestOptions().placeholder(avatar).circleCrop()
                    Glide
                        .with(requireActivity())
                        .load(StringHelper.validateEmpty(user.lastName))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(requestOption)
                        .error(avatar)
                        .into(binding.ivPhotoProfile)
                }
            }
        }

        viewModel.users.observe(requireActivity()) { user ->
            if (user != null) {
                binding.tvName.text(user.name)
                binding.tvEmail.text(user.email)

                val avatar = StringHelper.generateTextDrawable(
                    StringHelper.getInitial(user.name?.trim()),
                    ContextCompat.getColor(requireContext(), R.color.teal_200),
                    binding.ivPhotoProfile.measuredWidth
                )

                if (user.photo.isNullOrEmpty()) {
                    binding.ivPhotoProfile.setImageDrawable(avatar)
                } else {
                    val requestOption = RequestOptions().placeholder(avatar).circleCrop()
                    Glide
                        .with(requireActivity())
                        .load(StringHelper.validateEmpty(user.photo))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(requestOption)
                        .error(avatar)
                        .into(binding.ivPhotoProfile)
                }

            } else {
                binding.tvName.text("Username")
                binding.tvEmail.text("Email")
            }

        }
    }

    private fun logout() {
        viewModel.logout {
            activity?.finishAffinity()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }
    }
}