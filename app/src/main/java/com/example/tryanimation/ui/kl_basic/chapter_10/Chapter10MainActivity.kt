package com.example.tryanimation.ui.kl_basic.chapter_10

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityChapter10MainBinding
import com.example.tryanimation.ui.kl_basic.chapter_10.adapter.TrialAdapter
import com.example.tryanimation.ui.kl_basic.chapter_10.model.TrialData

class Chapter10MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChapter10MainBinding

    private lateinit var adapter: TrialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chapter10_main)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {

        val listData = arrayOf(
            TrialData(R.drawable.honkai_impact_3rd,
                "Kiana Kaslana",
                "Freya High School", "6281323784889"),
            TrialData(R.drawable.honkai_star_rail,
                "Bella Antrifosa",
                "Harvard",
                "6281323784889"),
            TrialData(R.drawable.clash_of_clans,
                "Michael Hoxa",
                "Stanford",
                "6281323784889"),
        )

        adapter = TrialAdapter(this, listData) { data ->
            val toDetail = Intent(this, DescriptionActivity::class.java).apply {
                putExtra("photo", data.photo)
                putExtra("name", data.name)
                putExtra("school", data.school)
                putExtra("phone", data.phone)
            }
            startActivity(toDetail)
        }

        binding.rvListFriend.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvListFriend.adapter = adapter

    }

}