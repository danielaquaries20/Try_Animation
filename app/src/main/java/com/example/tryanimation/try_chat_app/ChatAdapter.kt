package com.example.tryanimation.try_chat_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.R

class ChatAdapter(private val context: Context?, private val items: ArrayList<PersonModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_CHAT_ME = R.layout.item_rv_chat_from_me
    private val VIEW_TYPE_CHAT_OPPONENT = R.layout.item_rv_chat_from_opponent

    override fun getItemViewType(position: Int): Int {
        return if (items[position].type == 1) {
            VIEW_TYPE_CHAT_ME
        } else {
            VIEW_TYPE_CHAT_OPPONENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CHAT_ME) {
            ChatFromMe(LayoutInflater.from(context).inflate(viewType, parent, false))
        } else {
            ChatFromOpponent(LayoutInflater.from(context).inflate(viewType, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (items[position].type == 1) {
            val holderItem = ChatFromMe(holder.itemView)

            try {
                holderItem.tvChatMe.text = items[position].chat
                holderItem.tvTimeMe.text = items[position].time
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            val holderItem = ChatFromOpponent(holder.itemView)

            try {
                holderItem.tvChatOpponent.text = items[position].chat
                holderItem.tvTimeOpponent.text = items[position].time
                holderItem.tvNameOpponent.text = "From ${items[position].panggilan}"
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    companion object {
        class ChatFromMe(view: View) : RecyclerView.ViewHolder(view) {
            // Holds the TextView that will add each animal to
            val tvChatMe: TextView = view.findViewById(R.id.tvChatFromMe)
            val tvTimeMe: TextView = view.findViewById(R.id.tvTimeFromMe)
            val tvNameFromMe: TextView = view.findViewById(R.id.tvNameFromMe)

        }

        class ChatFromOpponent(view: View) : RecyclerView.ViewHolder(view) {
            // Holds the TextView that will add each animal to
            val tvChatOpponent: TextView = view.findViewById(R.id.tvChatFromOpponent)
            val tvTimeOpponent: TextView = view.findViewById(R.id.tvTimeFromOpponent)
            val tvNameOpponent: TextView = view.findViewById(R.id.tvNameFromOpponent)
        }
    }

}