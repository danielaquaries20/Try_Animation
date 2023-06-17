package com.example.tryanimation.ui.kl_basic.chapter_10.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityListFriendBinding
import com.example.tryanimation.model.Friend
import com.example.tryanimation.ui.kl_basic.chapter_10.adapter.AdapterRvFriend

class ListFriendActivity : AppCompatActivity() {

    //1 Membuat view RecyclerView nya di xml activity_list_friend
    //2 Membuat Item untuk RecyclerView kita
    //3 Membuat Data untuk Itemnya
    //4 Membuat Adapter dan ViewHolder untuk RecyclerView nya
    //5 Memasangkan Adapter ke Recycler View serta datanya ke Adapter

    private lateinit var adapterFriend: AdapterRvFriend

    private lateinit var binding: ActivityListFriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val listFriend = arrayOf(
            Friend(R.drawable.honkai_impact_3rd, "Kiana Kaslana", "Freya High School", "08675436298"),
            Friend(R.drawable.honkai_star_rail, "Bella Antrifosa", "Harvard", "0987654321"),
            Friend(R.drawable.clash_of_clans, "Michael Hoxa", "Stanford" ,"08675436298"),
        )

        adapterFriend = AdapterRvFriend(this, listFriend) { data ->
            val toDetail = Intent(this, DetailFriendActivity::class.java).apply {
                putExtra("photo", data.photo)
                putExtra("name", data.name)
                putExtra("school", data.school)
                putExtra("phone", data.phone)
            }
            startActivity(toDetail)
        }

        binding.rvListFriend.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListFriend.adapter = adapterFriend
    }
}