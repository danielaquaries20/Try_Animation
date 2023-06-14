package com.example.tryanimation.ui.kl_basic.chapter_10

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityDescriptionBinding
import com.example.tryanimation.ui.kl_basic.chapter_10.model.TrialData

class DescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_description)

        initView()
    }

    private fun initView() {
        val name = intent.getStringExtra("name").toString()
        val school = intent.getStringExtra("school").toString()
        val phone = intent.getStringExtra("phone").toString()
        val photo = intent.getIntExtra("photo", 0)
        binding.friend = TrialData(photo, name, school, phone)

        /*binding.tvName.text = name
        binding.tvSchool.text = school
        binding.ivIcon.setImageResource(photo)*/

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