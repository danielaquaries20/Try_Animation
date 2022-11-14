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
    private val VIEW_TYPE_LOADING = R.layout.item_rv_chat_loading

    private val VIEW_TYPE_CHAT_DATE = R.layout.item_rv_chat_date

    override fun getItemViewType(position: Int): Int {
        val theView = when (items[position].type) {
            1 -> {
                VIEW_TYPE_CHAT_ME
            }
            2 -> {
                VIEW_TYPE_CHAT_OPPONENT
            }
            3 -> {
                VIEW_TYPE_LOADING
            }
            else -> {
                VIEW_TYPE_CHAT_DATE
            }
        }

        return theView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val theViewHolder = when (viewType) {
            VIEW_TYPE_CHAT_ME -> {
                ChatFromMe(LayoutInflater.from(context).inflate(viewType, parent, false))
            }
            VIEW_TYPE_CHAT_OPPONENT -> {
                ChatFromOpponent(LayoutInflater.from(context).inflate(viewType, parent, false))
            }
            VIEW_TYPE_LOADING -> {
                ChatLoading(LayoutInflater.from(context).inflate(viewType, parent, false))
            }
            else -> {
                ChatDate(LayoutInflater.from(context).inflate(viewType, parent, false))
            }
        }

        /*if (viewType == VIEW_TYPE_CHAT_ME) {
            ChatFromMe(LayoutInflater.from(context).inflate(viewType, parent, false))
        } else {
            ChatFromOpponent(LayoutInflater.from(context).inflate(viewType, parent, false))
        }*/

        return theViewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (items[position].type) {
            1 -> {
                val holderItem = ChatFromMe(holder.itemView)

                try {
                    holderItem.tvChatMe.text = items[position].chat
                    holderItem.tvTimeMe.text = items[position].time
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            2 -> {
                val holderItem = ChatFromOpponent(holder.itemView)

                try {
                    holderItem.tvChatOpponent.text = items[position].chat
                    holderItem.tvTimeOpponent.text = items[position].time
                    holderItem.tvNameOpponent.text = "From ${items[position].panggilan}"
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            3 -> {
                val holderItem = ChatLoading(holder.itemView)
                try {
                    val typed = "${items[position].nama} sedang mengetik..."
                    holderItem.tvSedangMengetik.text = typed
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            else -> {
                val holderItem = ChatDate(holder.itemView)

                try {
                   holderItem.tvDate.text = items[position].date
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        /*if (items[position].type == 1) {
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
        }*/
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addLoading(who: String? = "") {
        if (!items.contains(PersonModel(type = 3))) {
            items.add(PersonModel(type = 3, nama = who))
            notifyDataSetChanged()
            //notifyItemInserted(items.lastIndex)
        }
    }

    fun removeLoading() {
        if (items.contains(PersonModel(type = 3))) {
            items.remove(PersonModel(type = 3))
            notifyDataSetChanged()
            //notifyItemRemoved(items.size)
        }
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

        class ChatLoading(view: View) : RecyclerView.ViewHolder(view) {
            // Holds the TextView that will add each animal to
            val tvSedangMengetik: TextView = view.findViewById(R.id.tvLoadingSedangMengetik)
        }

        class ChatDate(view: View) : RecyclerView.ViewHolder(view) {
            // Holds the TextView that will add each animal to
            val tvDate: TextView = view.findViewById(R.id.tvDate)
        }

    }

}