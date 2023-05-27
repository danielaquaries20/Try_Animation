package com.example.tryanimation.ui.kl_basic.chapter_8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityMyBiodataBinding
import com.example.tryanimation.model.Biodata

class MyBiodataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyBiodataBinding

    var kobin : String? = null

    protected var number : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_my_biodata)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_biodata)
        setContentView(binding.root)

        val myBiodata = Biodata("Daniel", "Jawa Tengah", "Main Game")

    }

    private fun mainProgram() {

    }

    override fun onStart() {
        super.onStart()
    }
}