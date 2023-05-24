package com.example.tryanimation.try_bottom_navigation

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.crocodic.core.base.activity.CoreActivity
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityTryBottomNavigationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TryBottomNavigationActivity :
    CoreActivity<ActivityTryBottomNavigationBinding, HomeViewModel>(R.layout.activity_try_bottom_navigation) {

//    private lateinit var bottomNav: BottomNavigationView

    private val homeFragment = HomeFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_try_bottom_navigation)

//        bottomNav = findViewById(R.id.bottomNavigationHome)

        replaceFragment(homeFragment)
        binding.bottomNavigationHome.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuHome -> {
                    replaceFragment(homeFragment)
                }
                R.id.menuProfile -> {
                    replaceFragment(profileFragment)
                }
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHomeContainer, fragment)
            commit()
        }
    }
}