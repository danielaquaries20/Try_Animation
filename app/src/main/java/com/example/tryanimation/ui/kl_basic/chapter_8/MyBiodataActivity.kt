package com.example.tryanimation.ui.kl_basic.chapter_8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityChapter8MainBinding
import com.example.tryanimation.databinding.ActivityMyBiodataBinding

class MyBiodataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyBiodataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_my_biodata)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_biodata)
        setContentView(binding.root)
    }
}