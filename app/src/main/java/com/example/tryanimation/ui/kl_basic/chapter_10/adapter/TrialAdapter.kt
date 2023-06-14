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
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: ItemTrialBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemTrialBinding.inflate(LayoutInflater.from(context), parent, false)
        return TrialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = TrialViewHolder(binding)

        Log.d("TrialAdapter", "onBindViewHolder: ${items[position]}")
        viewHolder.binding.ivPhoto.setImageResource(items[position].photo)
        viewHolder.binding.tvName.text = items[position].name
        viewHolder.binding.tvSchool.text = items[position].school

        viewHolder.itemView.setOnClickListener {
            onItemClick(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class TrialViewHolder(val binding: ItemTrialBinding) : RecyclerView.ViewHolder(binding.root)