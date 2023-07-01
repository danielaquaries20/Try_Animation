package com.example.tryanimation.ui.kl_basic.chapter_13.ui.list_contact

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.databinding.ItemTryAllBinding

class AdapterRvListContact(private val items: ArrayList<String>) :
    RecyclerView.Adapter<AdapterRvListContact.ViewHolderListContact>() {
    class ViewHolderListContact(val binding: ItemTryAllBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: String) {
            binding.contactName = contact
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderListContact {
        val binding = ItemTryAllBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderListContact(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderListContact, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}