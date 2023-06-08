package com.example.tryanimation.ui.kl_basic.chapter_10

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityChapter10MainBinding

class Chapter10MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityChapter10MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chapter10_main)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {
        binding.cardHi3Rd.setOnClickListener(this)
        binding.cardHsr.setOnClickListener(this)
        binding.cardCoc.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.cardHi3Rd -> goToDetailHI()
            binding.cardHsr -> goToDetailHSR()
            binding.cardCoc -> goToDetailCOC()
        }
    }

    private fun goToDetailHI() {
        val toDetail = Intent(this, DescriptionActivity::class.java).apply {
            putExtra("name", binding.tvHi3rd.text)
        }
        startActivity(toDetail)
    }

    private fun goToDetailHSR() {
        val toDetail = Intent(this, DescriptionActivity::class.java).apply {
            putExtra("name", binding.tvHsr.text)
        }
        startActivity(toDetail)
    }

    private fun goToDetailCOC() {
        val toDetail = Intent(this, DescriptionActivity::class.java).apply {
            putExtra("name", binding.tvCoc.text)
        }
        startActivity(toDetail)
    }


}