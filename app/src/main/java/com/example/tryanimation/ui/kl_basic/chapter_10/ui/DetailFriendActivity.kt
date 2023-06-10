package com.example.tryanimation.ui.kl_basic.chapter_10.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tryanimation.R

class DetailFriendActivity : AppCompatActivity() {

    private lateinit var ivPhoto: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvSchool: TextView
    private lateinit var btnCallWa: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_friend)

        ivPhoto = findViewById(R.id.iv_photo)
        tvName = findViewById(R.id.tv_name)
        tvSchool = findViewById(R.id.tv_school)
        btnCallWa = findViewById(R.id.btn_call_wa)

        val photo = intent.getIntExtra("photo", 0)
        val name = intent.getStringExtra("name")
        val school = intent.getStringExtra("school")
        val phone = intent.getStringExtra("phone")

        ivPhoto.setImageResource(photo)
        tvName.text = name
        tvSchool.text = school

        btnCallWa.setOnClickListener {
            val message = "Halo $name dari $school, apa kabar?"

            val callWa = Intent(Intent.ACTION_VIEW,
                Uri.parse(
                    "https://api.whatsapp.com/send?phone=$phone&text=$message"
                )
            )
            startActivity(callWa)
        }

    }
}