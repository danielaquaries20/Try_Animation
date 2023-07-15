package com.example.tryanimation.ui.kl_basic.chapter_12.ui.list_friend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tryanimation.databinding.ActivityListFriendDbBinding
import com.example.tryanimation.ui.kl_basic.chapter_12.data.database.FriendEntity
import com.example.tryanimation.ui.kl_basic.chapter_12.data.database.MyDatabase
import com.example.tryanimation.ui.kl_basic.chapter_12.ui.add_friend.AddFriendActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ListFriendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListFriendDbBinding

    private var adapter : AdapterRvFriendDb? = null
    private val listFriend = ArrayList<FriendEntity>()

    private lateinit var myDatabase: MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListFriendDbBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AdapterRvFriendDb(listFriend) { data->
            val toEdit = Intent(this, AddFriendActivity::class.java).apply {
                putExtra("name", data.name)
                putExtra("school", data.school)
                putExtra("hobby", data.hobby)
                putExtra("photo", data.photo)
                putExtra("id", data.id)
            }
            startActivity(toEdit)
        }
        binding.rvListFriend.adapter = adapter

        myDatabase = MyDatabase.getDatabase(this)

        lifecycleScope.launch {
            myDatabase.friendDao().getAll().collect {
                listFriend.clear()
                adapter?.notifyDataSetChanged()
                listFriend.addAll(it)
                adapter?.notifyItemInserted(0)
            }
        }

    }
}