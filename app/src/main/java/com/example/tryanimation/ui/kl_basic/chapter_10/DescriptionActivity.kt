package com.example.tryanimation.ui.kl_basic.chapter_10

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityDescriptionBinding

class DescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_description)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        val name = intent.getStringExtra("name")
        when (name) {
            getString(R.string.honkai_impact_3rd) -> {
                binding.ivIcon.setImageDrawable(ResourcesCompat.getDrawable(resources,
                    R.drawable.honkai_impact_3rd,
                    null))
            }
            getString(R.string.honkai_star_rail) -> {
                binding.ivIcon.setImageDrawable(ResourcesCompat.getDrawable(resources,
                    R.drawable.honkai_star_rail,
                    null))
            }
            getString(R.string.clash_of_clans) -> {
                binding.ivIcon.setImageDrawable(ResourcesCompat.getDrawable(resources,
                    R.drawable.clash_of_clans,
                    null))
            }
        }
        binding.tvName.text = name
    }
}