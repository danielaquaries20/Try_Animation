package com.example.tryanimation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TryDatePickerActivity : AppCompatActivity() {

    private lateinit var tvDateStart  : TextView
    private lateinit var tvDateEnd  : TextView
    private lateinit var btnPickDate  : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_date_picker)

        tvDateStart = findViewById(R.id.tvDateStart)
        tvDateEnd = findViewById(R.id.tvDateEnd)
        btnPickDate = findViewById(R.id.btnPickDate)


    }
}