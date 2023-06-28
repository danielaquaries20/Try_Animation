package com.example.tryanimation.ui.kl_basic.chapter_13.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityChapter13MainBinding
import com.example.tryanimation.ui.kl_basic.chapter_13.test_ui.contact.GetContactActivity
import com.example.tryanimation.ui.kl_basic.chapter_13.test_ui.gallery_and_camera.GalleryCameraActivity

class Chapter13MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChapter13MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChapter13MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnContact.setOnClickListener {
            startActivity(Intent(this, GetContactActivity::class.java))
        }

        binding.btnGalleryCamera.setOnClickListener {
            startActivity(Intent(this, GalleryCameraActivity::class.java))
        }
    }
}