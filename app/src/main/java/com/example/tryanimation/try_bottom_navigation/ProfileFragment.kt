package com.example.tryanimation.try_bottom_navigation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.crocodic.core.base.fragment.CoreFragment
import com.crocodic.core.extension.text
import com.example.tryanimation.R
import com.example.tryanimation.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : CoreFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private lateinit var viewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this@ProfileFragment)[HomeViewModel::class.java]

        observe()
    }

    private fun observe() {
        viewModel.user.observe(requireActivity()) { user ->
            if (user != null) {
                binding.tvIdUser.text("Id: ${user.id}")
                binding.tvName.text("Username: ${user.firstName} ${user.lastName}")
                binding.tvAge.text("Age: ${user.age}")
                binding.tvBio.text("Bio: ${user.bio}")
            }
        }
    }
}