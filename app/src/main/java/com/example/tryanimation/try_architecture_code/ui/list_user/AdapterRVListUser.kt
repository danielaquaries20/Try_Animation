package com.example.tryanimation.try_architecture_code.ui.list_user

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.R
import com.example.tryanimation.try_architecture_code.database.user.UserEntity
import com.example.tryanimation.try_architecture_code.ui.detail_user.DetailUserActivity

class AdapterRVListUser(private val context: Context) :
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
                val toEdit = Intent(context, DetailUserActivity::class.java).apply {
                    putExtra("isEdit", true)
                    putExtra("idUser", currentItem.id)
                    putExtra("firstName", currentItem.firstName)
                    putExtra("lastName", currentItem.lastName)
                    putExtra("age", currentItem.age)
                    putExtra("bio", currentItem.bio)
                }
                context.startActivity(toEdit)
//                Toast.makeText(context, "Item Clicked", Toast.LENGTH_SHORT).show()
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