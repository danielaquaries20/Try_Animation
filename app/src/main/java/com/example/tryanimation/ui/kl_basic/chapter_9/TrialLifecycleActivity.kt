package com.example.tryanimation.ui.kl_basic.chapter_9

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tryanimation.R
import com.example.tryanimation.ui.kl_basic.chapter_9.adapter.TabAdapter
import com.example.tryanimation.ui.kl_basic.chapter_9.fragments.TrialOneFragment
import com.example.tryanimation.ui.kl_basic.chapter_9.fragments.TrialTwoFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TrialLifecycleActivity : AppCompatActivity() {

    //1 Layouting
    //2 Membuat class Fragment
    //3 Membuat Adapter
    //4 Mendeklarasikannya

    private lateinit var tabHome: TabLayout
    private lateinit var viewPagerHome: ViewPager2

    private lateinit var tvNumber: TextView

    private var number: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("TestLifecycle", "1")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trial_lifecycle)

        tabHome = this.findViewById(R.id.tab_layout_home)
        viewPagerHome = findViewById(R.id.view_pager_home)

        val arrayFragment = arrayOf(
            TrialOneFragment() as Fragment,
            TrialTwoFragment() as Fragment
        )

        val arrayTitle = arrayOf("Sample One", "Sample Two")

        val adapterHome = TabAdapter(supportFragmentManager, lifecycle, arrayFragment)
        viewPagerHome.adapter = adapterHome

        TabLayoutMediator(tabHome, viewPagerHome) { tab, position ->
            tab.text = arrayTitle[position]
        }.attach()



        val intentToHome = Intent(this, Chapter9MainActivity::class.java)
        startActivity(intentToHome)

    }

    override fun onStart() {
        super.onStart()
        Log.d("TestLifecycle", "2")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TestLifecycle", "3")
    }

    override fun onPause() {
        super.onPause()
        number ++
        tvNumber.text = number.toString()
    }

    override fun onStop() {
        super.onStop()
        Log.d("TestLifecycle", "5")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TestLifecycle", "6")
    }


}