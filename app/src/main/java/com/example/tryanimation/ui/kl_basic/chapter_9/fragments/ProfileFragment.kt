package com.example.tryanimation.ui.kl_basic.chapter_9.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.tryanimation.R
import com.example.tryanimation.databinding.FragmentProfile2Binding
import com.example.tryanimation.ui.kl_basic.chapter_8.model.Person


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfile2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile2, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        val person = Person(
            name = "Kiana Kaslana",
            description = "Semangat tempur luar biasa..!",
            gender = "Perempuan",
            address = "Semarang, Jawa Tengah",
            birthPlace = "Bandung, Jawa Barat",
            hobby = "Bermain Game",
            socialMedia = "kiankas@gmail.com"
        )

//        tvName.text = person.name

        binding.tvUsername.text = person.name
        binding.tvDescription.text = person.description
        binding.tvGender.text = person.gender
        binding.tvAddress.text = person.address
        binding.tvBirthPlace.text = person.birthPlace
        binding.tvHobby.text = person.hobby
        binding.tvSocialMedia.text = person.socialMedia

    }

}