package com.example.tryanimation.ui.kl_basic.chapter_12.ui.test_ui.test_db

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tryanimation.databinding.ActivityInsertDatabaseBinding
import com.example.tryanimation.ui.kl_basic.chapter_12.data.test_data.room_database.TryFriendEntity
import com.example.tryanimation.ui.kl_basic.chapter_12.data.test_data.room_database.TryMyDatabase
import kotlinx.coroutines.launch

class InsertDatabaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertDatabaseBinding

    private lateinit var myDatabase: TryMyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertDatabaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDatabase = TryMyDatabase.getInstance(this)

        initView()

    }

    private fun initView() {
        binding.btnSave.setOnClickListener { saveData() }
    }

    private fun saveData() {
        val name = binding.etName.text.trim().toString()
        val school = binding.etSchool.text.trim().toString()
        val hobby = binding.etHobby.text.trim().toString()

        if (name.isEmpty()) {
            binding.etName.error = "Fill the name form"
            return
        }

        if (school.isEmpty()) {
            binding.etSchool.error = "Fill the school form"
            return
        }

        if (hobby.isEmpty()) {
            binding.etHobby.error = "Fill the hobby form"
            return
        }

        val friend = TryFriendEntity(name, school, hobby)
        lifecycleScope.launch {
            myDatabase.tryFriendDao().insert(friend)
            Toast.makeText(this@InsertDatabaseActivity, "Succeed", Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}