package com.example.tryanimation.try_architecture_code

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tryanimation.R

class TryArchitectureCodeActivity : AppCompatActivity() {

    private lateinit var viewModel: TryArchitectureViewModel

    private lateinit var btnTambah: Button
    private lateinit var btnKurang: Button

    private lateinit var tvAngka: TextView
    private lateinit var tvBoolean: TextView

    private var number: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_architecture_code)

        viewModel = ViewModelProvider(this)[TryArchitectureViewModel::class.java]

        btnTambah = findViewById(R.id.btnTambah)
        btnKurang = findViewById(R.id.btnKurang)
        tvAngka = findViewById(R.id.tvLiveInt)
        tvBoolean = findViewById(R.id.tvLiveBoolean)

        observe()
        initClick()
    }

    private fun observe() {
        viewModel.currentNumber.observe(this) { num ->
            number = num
            tvAngka.text = num.toString()
        }
        viewModel.currentBoolean.observe(this) { bool ->
            if (bool) {
                tvBoolean.text = "Angka habis jika dibagi 2"
            } else {
                tvBoolean.text = "Angka tidak habis jika dibagi 2"
            }

            if (number == 0) {
                tvBoolean.text = "Angka masih kosong"
            }
        }
    }

    private fun initClick() {
        btnTambah.setOnClickListener {
            viewModel.increase(number)
        }
        btnKurang.setOnClickListener {
            viewModel.decrease(number)
        }

    }

}