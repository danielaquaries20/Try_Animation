package com.example.tryanimation.try_bottom_navigation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.extension.createIntent
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.tos
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityTryBottomNavigationBinding
import com.example.tryanimation.try_architecture_code.ui.note.add.AddNoteActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

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

        binding.fabAddNote.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v) {
            binding.fabAddNote -> {
                val toAddNote = createIntent<AddNoteActivity>()
                activityLauncher.launch(toAddNote) { result ->
                    if (result.resultCode == 100) {
                        lifecycleScope.launch {
                            replaceFragment(profileFragment)
                            delay(200)
                            replaceFragment(homeFragment)
                        }
                    }
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHomeContainer, fragment)
            commit()
        }
    }



}