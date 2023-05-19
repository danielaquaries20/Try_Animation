package com.example.tryanimation.ui.kl_basic.chapter_7

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityChapter7MainBinding
import com.google.android.material.snackbar.Snackbar

class Chapter7MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityChapter7MainBinding

    private var nominalData: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chapter7_main)
        setContentView(binding.root)
        initView()
    }


    private fun initView() {
        binding.etNominal.doOnTextChanged { text, start, before, count ->
            nominalData = if (!text.isNullOrEmpty()) {
                val nominal = text.toString().toInt()
                if (nominal > 99) {
                    binding.etNominal.setText("99")
                    Snackbar.make(this,
                        binding.etNominal,
                        "Batas maksimal 99",
                        Snackbar.LENGTH_SHORT).show()
                }
                nominal
            } else {
                0
            }

        }

        binding.btnAddImage.setOnClickListener(this)
    }

    private fun showImage() {
        binding.linearForm.removeAllViews()
        if (nominalData >= 1) {
            for (i in 1..nominalData) {
                /*if (image?.parent != null) {
                    ((image!!.parent) as ViewGroup).removeView(image)
                }*/
                val inflater = LayoutInflater.from(this)
                val image =
                    inflater.inflate(R.layout.item_image_nominal, null, false) as LinearLayout

                val tvIndex = image.findViewById<TextView>(R.id.tvIndex)
                tvIndex.text = "-> $i"

                binding.linearForm.addView(image)
            }
        }

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnAddImage -> showImage()
        }
    }
}