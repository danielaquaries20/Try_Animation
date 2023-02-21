package com.example.tryanimation.try_huawei_oauth

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tryanimation.R

class TryHuaweiOauthActivity : AppCompatActivity() {

    private lateinit var btnTryOauth: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_huawei_oauth)

        btnTryOauth = findViewById(R.id.btnTry)

        btnTryOauth.setOnClickListener {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}