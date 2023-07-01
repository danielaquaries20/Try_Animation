package com.example.tryanimation.ui.kl_basic.chapter_12.ui.test_ui.test_db

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.databinding.ItemTryListDatabaseBinding
import com.example.tryanimation.ui.kl_basic.chapter_12.data.test_data.room_database.TryFriendEntity

class TryAdapterRvDatabase(private val items: List<TryFriendEntity>, val onClickItem: (TryFriendEntity) -> Unit) :
    RecyclerView.Adapter<TryAdapterRvDatabase.TryViewHolderRvDatabase>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TryViewHolderRvDatabase {
        return TryViewHolderRvDatabase(ItemTryListDatabaseBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: TryViewHolderRvDatabase, position: Int) {
        holder.binding.tvName.text = items[position].name
        holder.binding.tvSchool.text = items[position].school
        holder.binding.tvHobby.text = items[position].hobby

        holder.itemView.setOnClickListener { onClickItem(items[position]) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class TryViewHolderRvDatabase(val binding: ItemTryListDatabaseBinding) :
        RecyclerView.ViewHolder(binding.root)

}