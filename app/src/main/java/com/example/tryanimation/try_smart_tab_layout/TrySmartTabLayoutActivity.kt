package com.example.tryanimation.try_smart_tab_layout

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.tryanimation.R
import com.example.tryanimation.try_bottom_navigation.HomeFragment
import com.example.tryanimation.try_bottom_navigation.ProfileFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.ogaclejapan.smarttablayout.SmartTabLayout


class TrySmartTabLayoutActivity : AppCompatActivity() {


    private lateinit var rootLayout: ConstraintLayout
    private lateinit var ivBack: ImageView
    private lateinit var smartTabLayout: SmartTabLayout
    private lateinit var materialTabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_smart_tab_layout)

        initView()
    }

    private fun initView() {
        rootLayout = findViewById(R.id.rootLayout)
        ivBack = findViewById(R.id.ivBack)

        initClick()
        initTabView()
//        initTabView2()
    }

    private fun initClick() {
        ivBack.setOnClickListener { onBackPressed() }
    }

    private fun initTabView() {
        smartTabLayout = findViewById(R.id.smartTabLayout)
        viewPager = findViewById(R.id.viewpager)

        smartTabLayout.isVisible = true
        viewPager.isVisible = true

        val fragments = arrayOf(HomeFragment(),
            ProfileFragment())

        val adapter = PagerAdapter(supportFragmentManager, fragments)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2
        smartTabLayout.setViewPager(viewPager)

    }

    private fun initTabView2() {
        materialTabLayout = findViewById(R.id.materialTabLayout)
        viewPager2 = findViewById(R.id.viewpager2)

        val listFragment = arrayOf(HomeFragment(), ProfileFragment())
        val adapter = ViewPager2Adapter(listFragment, supportFragmentManager, lifecycle)

        materialTabLayout.isVisible = true
        viewPager2.isVisible = true

        materialTabLayout.addTab(materialTabLayout.newTab().setText("Home")
            .setIcon(R.drawable.ic_baseline_home))
        materialTabLayout.addTab(materialTabLayout.newTab().setText("Profile")
            .setIcon(R.drawable.ic_baseline_person))

        viewPager2.adapter = adapter

        materialTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewPager2.currentItem = it.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val data = tab?.text
                showContent("Unselected: $data")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val data = tab?.text
                showContent("Reselected: $data")
            }
        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                materialTabLayout.selectTab(materialTabLayout.getTabAt(position))
            }
        })
    }

    private fun showContent(content: String, isSnack: Boolean = true, withLog: Boolean = true) {
        if (isSnack) {
            Snackbar.make(rootLayout, content, Snackbar.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
        }

        if (withLog) {
            Log.d("TryTabLayout", content)
        }
    }

}