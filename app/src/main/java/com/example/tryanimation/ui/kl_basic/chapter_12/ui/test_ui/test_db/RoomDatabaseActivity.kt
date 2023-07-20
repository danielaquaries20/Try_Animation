package com.example.tryanimation.ui.kl_basic.chapter_12.ui.test_ui.test_db

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tryanimation.databinding.ActivityRoomDatabaseBinding
import com.example.tryanimation.ui.kl_basic.chapter_12.data.test_data.room_database.TryFriendEntity
import com.example.tryanimation.ui.kl_basic.chapter_12.data.test_data.room_database.TryMyDatabase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RoomDatabaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoomDatabaseBinding

    private lateinit var myDatabase: TryMyDatabase

    private var adapter: TryAdapterRvDatabase? = null

    private val listFriend = ArrayList<TryFriendEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomDatabaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDatabase = TryMyDatabase.getInstance(this)

        initView()
        collect()

    }

    private fun initView() {
        adapter = TryAdapterRvDatabase(listFriend) { data ->
            val toEdit = Intent(this, InsertDatabaseActivity::class.java).apply {
                putExtra("isEdit", true)
                putExtra("id", data.id)
            }
            startActivity(toEdit)
        }
        binding.rvListFriend.adapter = adapter

        binding.btnAdd.setOnClickListener {
            val toAdd = Intent(this, InsertDatabaseActivity::class.java).apply {
                putExtra("isEdit", false)
            }
            startActivity(toAdd)
        }
    }

    private fun collect() {
        lifecycleScope.launch {
            myDatabase.tryFriendDao().getAll().collect {
                listFriend.clear()
                adapter?.notifyDataSetChanged()
                listFriend.addAll(it)
                adapter?.notifyItemInserted(0)
            }
        }
    }
}