package com.example.tryanimation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MotionLayoutOneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_layout_one)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.transition_from_left,R.anim.transition_to_right)
    }

}