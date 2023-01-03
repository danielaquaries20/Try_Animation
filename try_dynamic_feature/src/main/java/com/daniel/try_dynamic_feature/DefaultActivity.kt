package com.daniel.try_dynamic_feature

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DefaultActivity : AppCompatActivity() {

    private lateinit var tvNumber: TextView
    private lateinit var btnDecrement: Button
    private lateinit var btnIncrement: Button

    private var number: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default)

        tvNumber = findViewById(R.id.tvNumber)
        btnDecrement = findViewById(R.id.btnDecrement)
        btnIncrement = findViewById(R.id.btnIncrement)

        initClick()
        tvNumber.text = number.toString()
    }

    private fun initClick() {
        btnIncrement.setOnClickListener {
            number++
            tvNumber.text = number.toString()
        }

        btnDecrement.setOnClickListener {
            number--
            tvNumber.text = number.toString()
        }

    }
}