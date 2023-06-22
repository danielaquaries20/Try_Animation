package com.example.tryanimation.ui.kl_basic.chapter_12.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityChapter10MainBinding
import com.example.tryanimation.databinding.ActivityChapter12MainBinding
import com.example.tryanimation.ui.kl_basic.chapter_12.ui.test_ui.test_db.RoomDatabaseActivity
import com.example.tryanimation.ui.kl_basic.chapter_12.ui.test_ui.test_sp.SharedPreferencesActivity

class Chapter12MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChapter12MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChapter12MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.btnSharedPreferences.setOnClickListener {
            startActivity(Intent(this, SharedPreferencesActivity::class.java))
        }
        binding.btnRoomDatabase.setOnClickListener {
            startActivity(Intent(this, RoomDatabaseActivity::class.java))
        }
    }
}