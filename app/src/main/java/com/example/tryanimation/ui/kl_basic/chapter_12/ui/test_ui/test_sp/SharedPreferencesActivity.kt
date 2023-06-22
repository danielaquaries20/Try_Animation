package com.example.tryanimation.ui.kl_basic.chapter_12.ui.test_ui.test_sp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tryanimation.databinding.ActivitySharedPreferencesBinding
import com.example.tryanimation.ui.kl_basic.chapter_12.data.test_data.shared_preferences.TestSharedPreferences

class SharedPreferencesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySharedPreferencesBinding

    private lateinit var mySP: TestSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mySP = TestSharedPreferences(this)
        initView()
    }

    private fun initView() {
        val savedName = mySP.getName()
        binding.tvName.text = savedName

        binding.btnSave.setOnClickListener { saveName() }
    }

    private fun saveName() {
        val name = binding.etInputName.text.toString()
        if (name.isEmpty()) {
            binding.etInputName.error = "Fill name form"
            return
        }

        mySP.saveName(name)
        binding.tvName.text = name
    }
}