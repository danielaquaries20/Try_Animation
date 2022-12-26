package com.example.tryanimation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class TryStickyScrollActivity : AppCompatActivity() {

    private lateinit var ivBack : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_sticky_scroll)

        ivBack = findViewById(R.id.ivBack)

        ivBack.setOnClickListener { onBackPressed() }
    }
}