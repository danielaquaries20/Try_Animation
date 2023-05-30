package com.example.tryanimation.ui.kl_basic.chapter_9

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityOnPauseBinding
import com.example.tryanimation.ui.kl_basic.chapter_9.fragments.ProfileFragment

class OnPauseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnPauseBinding

    private var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_pause)
        setContentView(binding.root)

        setProfile()
    }

    private fun setProfile() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_profile, ProfileFragment())
            commit()
        }
    }

    override fun onPause() {
        super.onPause()
        increasePauseCount()
    }

    private fun increasePauseCount() {
        counter++
        binding.tvCounter.text = counter.toString()
    }

}