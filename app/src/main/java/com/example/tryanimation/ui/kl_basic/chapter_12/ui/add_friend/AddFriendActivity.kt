package com.example.tryanimation.ui.kl_basic.chapter_12.ui.add_friend

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tryanimation.databinding.ActivityAddFriendBinding
import com.example.tryanimation.ui.kl_basic.chapter_12.data.database.FriendEntity
import com.example.tryanimation.ui.kl_basic.chapter_12.data.database.MyDatabase
import kotlinx.coroutines.launch

class AddFriendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFriendBinding

    private lateinit var myDatabase: MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDatabase = MyDatabase.getDatabase(this)

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.trim().toString()
            val school = binding.etSchool.text.trim().toString()
            val hobby = binding.etHobby.text.trim().toString()

            if (name.isEmpty()) {
                binding.etName.error = "Fill blank form"
                return@setOnClickListener
            }

            if (school.isEmpty()) {
                binding.etSchool.error = "Fill blank form"
                return@setOnClickListener
            }

            if (hobby.isEmpty()) {
                binding.etHobby.error = "Fill blank form"
                return@setOnClickListener
            }

            val newFriend = FriendEntity(name, school, hobby)

            lifecycleScope.launch {
                myDatabase.friendDao().insert(newFriend)
                Toast.makeText(this@AddFriendActivity, "Succeed", Toast.LENGTH_SHORT).show()
                finish()
            }

        }

    }
}