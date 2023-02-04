package com.example.tryanimation.try_bottom_navigation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tryanimation.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class TryBottomNavigationActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    private val homeFragment = HomeFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_bottom_navigation)

        bottomNav = findViewById(R.id.bottomNavigationHome)

        replaceFragment(homeFragment)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuHome -> {
                    Toast.makeText(this, "Sweet 'Home' Alabama", Toast.LENGTH_SHORT).show()
                    replaceFragment(homeFragment)
                }
                R.id.menuProfile -> {
                    Toast.makeText(this, "Strange 'Profile', huh?", Toast.LENGTH_SHORT).show()
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