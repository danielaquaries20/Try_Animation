package com.example.tryanimation.ui.kl_basic.chapter_12.ui.saved_name

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivitySavedNameBinding
import com.example.tryanimation.ui.kl_basic.chapter_12.data.shared_pref.MySharedPreferences

class SavedNameActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySavedNameBinding

    private lateinit var mySp : MySharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mySp = MySharedPreferences(this)

        binding.btnSave.setOnClickListener {
            val name = binding.etInputName.text.trim().toString()
            if (name.isEmpty()) {
                binding.etInputName.error = "Fill blank form"
            } else {
                mySp.setName(name)
                binding.tvName.text = name
            }
        }


        binding.tvName.text = mySp.getName()
    }
}