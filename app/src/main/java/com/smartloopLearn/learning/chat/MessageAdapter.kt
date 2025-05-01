package com.smartloopLearn.learning.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartloopLearn.learning.R

class MessageAdapter(
    private val messageList: List<Message>,
    private val currentUserId: String
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val leftMessage: TextView = itemView.findViewById(R.id.leftMessage)
        val rightMessage: TextView = itemView.findViewById(R.id.rightMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]

        if (message.senderId == currentUserId) {
            // Sent by current user → show on right
            holder.rightMessage.text = message.message
            holder.rightMessage.visibility = View.VISIBLE
            holder.leftMessage.visibility = View.GONE
        } else {
            // Received message → show on left
            holder.leftMessage.text = message.message
            holder.leftMessage.visibility = View.VISIBLE
            holder.rightMessage.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = messageList.size
}
