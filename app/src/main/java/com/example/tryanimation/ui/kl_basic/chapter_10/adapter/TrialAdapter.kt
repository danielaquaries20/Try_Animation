package com.example.tryanimation.ui.kl_basic.chapter_10.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.R
import com.example.tryanimation.ui.kl_basic.chapter_10.model.TrialData

class TrialAdapter(
    private val context: Context,
    private val items: Array<TrialData>,
    private val onItemClick: (data: TrialData) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TrialViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_trial, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = TrialViewHolder(holder.itemView)

        viewHolder.ivPhoto.setImageResource(items[position].photo)
        viewHolder.tvName.text = items[position].name
        viewHolder.tvSchool.text = items[position].school

        viewHolder.itemView.setOnClickListener {
            onItemClick(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class TrialViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivPhoto: ImageView = view.findViewById(R.id.iv_photo)
    val tvName: TextView = view.findViewById(R.id.tv_name)
    val tvSchool: TextView = view.findViewById(R.id.tv_school)
}