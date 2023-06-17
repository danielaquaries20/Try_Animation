package com.example.tryanimation.ui.kl_basic.chapter_10.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.databinding.ItemTrialBinding
import com.example.tryanimation.ui.kl_basic.chapter_10.model.TrialData

class TrialAdapter(
    private val context: Context,
    private val items: Array<TrialData>,
    private val onItemClick: (data: TrialData) -> Unit,
) : RecyclerView.Adapter<TrialAdapter.TrialViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrialViewHolder {
        val binding = ItemTrialBinding.inflate(LayoutInflater.from(context), parent, false)
        return TrialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrialViewHolder, position: Int) {
        Log.d("TrialAdapter", "onBindViewHolder: ${items[position]}")
        holder.binding.ivPhoto.setImageResource(items[position].photo)
        holder.binding.tvName.text = items[position].name
        holder.binding.tvSchool.text = items[position].school

        holder.itemView.setOnClickListener {
            onItemClick(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class TrialViewHolder(val binding: ItemTrialBinding) : RecyclerView.ViewHolder(binding.root)
}

