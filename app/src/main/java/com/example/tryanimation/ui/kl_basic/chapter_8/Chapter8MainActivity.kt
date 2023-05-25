package com.example.tryanimation.ui.kl_basic.chapter_8

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityChapter8MainBinding
import com.example.tryanimation.ui.kl_basic.chapter_8.model.Person

class Chapter8MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChapter8MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chapter8_main)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        val person = Person(
            name = "Kiana Kaslana",
            description = "Semangat tempur luar biasa..!",
            gender = "Perempuan",
            address = "Semarang, Jawa Tengah",
            birthPlace = "Bandung, Jawa Barat",
            hobby = "Bermain Game",
            socialMedia = "kiankas@gmail.com"
        )

        binding.tvUsername.text = person.name
        binding.tvDescription.text = person.description
        binding.tvGender.text = person.gender
        binding.tvAddress.text = person.address
        binding.tvBirthPlace.text = person.birthPlace
        binding.tvHobby.text = person.hobby
        binding.tvSocialMedia.text = person.socialMedia

    }
}