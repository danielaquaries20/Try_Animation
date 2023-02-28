package com.example.tryanimation.try_architecture_code.ui.list_user

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.R
import com.example.tryanimation.try_architecture_code.database.user.UserEntity

class AdapterRVListUser(
    private val context: Context,
    private val onItemClick: (data: UserEntity, view: View) -> Unit,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listItem = emptyList<UserEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_rv_user, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            val currentItem = listItem[position]
            val holderItem = MyViewHolder(holder.itemView)

            holderItem.tvUserId.text = currentItem.id.toString()
            val username = "${currentItem.firstName} ${currentItem.lastName}"
            holderItem.tvUsername.text = username
            if (currentItem.age != null) {
                holderItem.tvUserAge.visibility = View.VISIBLE
                holderItem.tvUserAge.text = "${currentItem.age} years old"
            } else {
                holderItem.tvUserAge.visibility = View.GONE
            }

            if (currentItem.bio.isNullOrEmpty()) {
                holderItem.tvBio.visibility = View.GONE
            } else {
                holderItem.tvBio.visibility = View.VISIBLE
                holderItem.tvBio.text = currentItem.bio.toString()
            }

            holderItem.itemView.setOnClickListener {
                onItemClick(currentItem, it)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    fun setData(listUser: List<UserEntity>) {
        this.listItem = listUser
        notifyDataSetChanged()
    }

    companion object {

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val tvUserId: TextView = view.findViewById(R.id.tvIdUser)
            val tvUsername: TextView = view.findViewById(R.id.tvUsername)
            val tvUserAge: TextView = view.findViewById(R.id.tvAge)
            val tvBio: TextView = view.findViewById(R.id.tvBio)

        }

    }

}