package com.example.tryanimation.ui.kl_basic.chapter_13.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.databinding.ItemDbFriendBinding
import com.example.tryanimation.databinding.ItemTryAllBinding

class AdapterRvContact(private val contacts: List<String>) :
    RecyclerView.Adapter<AdapterRvContact.ViewHolderRvContact>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolderRvContact {
        val binding =
            ItemTryAllBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderRvContact(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderRvContact, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int {
        return contacts.size
    }


    class ViewHolderRvContact(val binding: ItemTryAllBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(value: String) {
                binding.contactName = value
                binding.executePendingBindings()
            }
        }

}