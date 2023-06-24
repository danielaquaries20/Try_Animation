package com.example.tryanimation.ui.kl_basic.chapter_12.ui.list_friend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.databinding.ItemDbFriendBinding
import com.example.tryanimation.ui.kl_basic.chapter_12.data.database.FriendEntity

class AdapterRvFriendDb(private val items: List<FriendEntity>) :
    RecyclerView.Adapter<AdapterRvFriendDb.ViewHolderFriendDb>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFriendDb {
        return ViewHolderFriendDb(ItemDbFriendBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolderFriendDb, position: Int) {
        holder.binding.tvName.text = items[position].name
        holder.binding.tvSchool.text = items[position].school
        holder.binding.tvHobby.text = items[position].hobby
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolderFriendDb(val binding: ItemDbFriendBinding) :
        RecyclerView.ViewHolder(binding.root)
}

