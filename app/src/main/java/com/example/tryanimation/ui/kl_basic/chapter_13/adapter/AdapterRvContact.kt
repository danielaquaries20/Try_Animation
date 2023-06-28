package com.example.tryanimation.ui.kl_basic.chapter_13.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.databinding.ItemDbFriendBinding

class AdapterRvContact(private val contacts: List<String>) :
    RecyclerView.Adapter<AdapterRvContact.ViewHolderRvContact>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolderRvContact {
        val binding =
            ItemDbFriendBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolderRvContact(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderRvContact, position: Int) {
        holder.binding.tvName.visibility = View.VISIBLE
        holder.binding.tvName.text = contacts[position]
        holder.binding.tvHobby.visibility = View.GONE
        holder.binding.tvSchool.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return contacts.size
    }


    class ViewHolderRvContact(val binding: ItemDbFriendBinding) :
        RecyclerView.ViewHolder(binding.root)

}