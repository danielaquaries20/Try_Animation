package com.example.tryanimation.ui.kl_basic.chapter_10.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.R
import com.example.tryanimation.ui.kl_basic.chapter_10.model.TrialFriend

class AdapterTrialFriend(
    private val context: Context,
    private val items: Array<TrialFriend>,
    private val onItemClick: (TrialFriend) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FriendTrialViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_trial_friend, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val friendViewHolder = FriendTrialViewHolder(holder.itemView)

        friendViewHolder.ivPhoto.setImageResource(items[position].photo)
        friendViewHolder.tvName.text = items[position].name
        friendViewHolder.tvSchool.text = items[position].school

        friendViewHolder.itemView.setOnClickListener {
            onItemClick(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class FriendTrialViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivPhoto: ImageView = view.findViewById(R.id.iv_photo)
    val tvName: TextView = view.findViewById(R.id.tv_name)
    val tvSchool: TextView = view.findViewById(R.id.tv_school)
}