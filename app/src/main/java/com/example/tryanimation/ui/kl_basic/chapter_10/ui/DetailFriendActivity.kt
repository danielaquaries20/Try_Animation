package com.example.tryanimation.ui.kl_basic.chapter_10.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityDetailFriendBinding

class DetailFriendActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailFriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_friend)

        binding.photo = intent.getIntExtra("photo", 0)
        binding.name = intent.getStringExtra("name")
        binding.school = intent.getStringExtra("school")
        val phone = intent.getStringExtra("phone")

        binding.btnCallWa.setOnClickListener {
            val message = "Halo ${binding.name} dari ${binding.school}, apa kabar?"

            val callWa = Intent(Intent.ACTION_VIEW,
                Uri.parse(
                    "https://api.whatsapp.com/send?phone=$phone&text=$message"
                )
            )
            startActivity(callWa)
        }

    }
}