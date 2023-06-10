package com.example.tryanimation.ui.kl_basic.chapter_10

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityDescriptionBinding

class DescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_description)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        val name = intent.getStringExtra("name")
        val school = intent.getStringExtra("school")
        val phone = intent.getStringExtra("phone")
        val photo = intent.getIntExtra("photo", 0)
        binding.tvName.text = name
        binding.tvSchool.text = school
        binding.ivIcon.setImageResource(photo)

        binding.btnCallWa.setOnClickListener {
            val message = "Halo $name dari $school, apa kabar?"
            val toWa = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "https://api.whatsapp.com/send?phone=$phone&text=$message"
                )
            )
            startActivity(toWa)

        }

    }
}