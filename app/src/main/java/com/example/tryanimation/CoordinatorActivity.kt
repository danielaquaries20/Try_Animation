package com.example.tryanimation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class CoordinatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.transition_from_right, R.anim.transition_zoom_out)
    }

}