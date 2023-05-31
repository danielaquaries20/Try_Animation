package com.example.tryanimation.ui.kl_basic.chapter_9

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityChapter9MainBinding
import com.example.tryanimation.ui.kl_basic.chapter_9.adapter.ViewPager2Adapter
import com.example.tryanimation.ui.kl_basic.chapter_9.fragments.SimpleOneFragment
import com.example.tryanimation.ui.kl_basic.chapter_9.fragments.SimpleTwoFragment
import com.google.android.material.tabs.TabLayoutMediator

class Chapter9MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChapter9MainBinding

    private lateinit var pageAdapter: ViewPager2Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chapter9_main)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {
        val pages = arrayOf(SimpleOneFragment() as Fragment, SimpleTwoFragment() as Fragment)

        val titlesPage = arrayOf("Simple One", "Simple Two")

        pageAdapter = ViewPager2Adapter(supportFragmentManager, lifecycle, pages)
        binding.viewPager.adapter = pageAdapter

        TabLayoutMediator(binding.tabTitle, binding.viewPager) { tab, position ->
            tab.text = titlesPage[position]
        }.attach()

        binding.btnAdd.setOnClickListener {
            val onPauseActivityIntent = Intent(this, OnPauseActivity::class.java)
            startActivity(onPauseActivityIntent)
        }
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(this, "On Start", Toast.LENGTH_SHORT).show()

        Log.d("CheckLifecycle", "OnStart")
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this, "On Pause", Toast.LENGTH_SHORT).show()
        Log.d("CheckLifecycle", "OnPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "On Destroy", Toast.LENGTH_SHORT).show()
        Log.d("CheckLifecycle", "OnDestroy")

    }
}