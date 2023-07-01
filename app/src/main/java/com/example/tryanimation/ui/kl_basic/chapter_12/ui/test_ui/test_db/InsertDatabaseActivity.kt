package com.example.tryanimation.ui.kl_basic.chapter_12.ui.test_ui.test_db

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
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

    private var dataFriend: TryFriendEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertDatabaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDatabase = TryMyDatabase.getInstance(this)

        initView()

    }

    private fun initView() {
        binding.btnSave.setOnClickListener { saveData() }

        dataFriend = TryFriendEntity(intent.getStringExtra("name"),
            intent.getStringExtra("school"),
            intent.getStringExtra("hobby")).apply {
            id = intent.getIntExtra("id", 0)
        }

        if (isEdit()) {
            binding.etName.setText(dataFriend?.name)
            binding.etSchool.setText(dataFriend?.school)
            binding.etHobby.setText(dataFriend?.hobby)
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnDelete.setOnClickListener { deleteFriend() }
        }

    }


    private fun isEdit() : Boolean {
        return dataFriend != null && dataFriend?.id != 0
    }

    private fun deleteFriend() {
        dataFriend?.let {friend ->
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Konfirmasi Delete")
            builder.setMessage("Apakah kamu yakin ingin menghapus ${friend.name}?")

            builder.setPositiveButton("Hapus") { dialog: DialogInterface, which: Int ->
                lifecycleScope.launch {
                    myDatabase.tryFriendDao().delete(friend)
                    dialog.dismiss()
                    Toast.makeText(this@InsertDatabaseActivity, "Deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            builder.setNegativeButton("Batal") { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
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


        if (isEdit()) {
            if (dataFriend?.name == name && dataFriend?.school == school && dataFriend?.hobby == hobby) {
                Toast.makeText(this, "Tidak ada perubahan data", Toast.LENGTH_SHORT).show()
            } else {
                val updatedFriend = TryFriendEntity(name, school, hobby).apply {
                    id = dataFriend?.id!!
                }
                lifecycleScope.launch {
                    myDatabase.tryFriendDao().update(updatedFriend)
                    Toast.makeText(this@InsertDatabaseActivity, "Updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

        } else {
            val friend = TryFriendEntity(name, school, hobby)
            lifecycleScope.launch {
                myDatabase.tryFriendDao().insert(friend)
                Toast.makeText(this@InsertDatabaseActivity, "Inserted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }
}