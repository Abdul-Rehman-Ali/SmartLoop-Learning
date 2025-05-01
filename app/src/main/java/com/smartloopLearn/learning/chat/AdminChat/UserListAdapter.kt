package com.smartloopLearn.learning.chat.AdminChat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartloopLearn.learning.databinding.RvItemUserListBinding

class UserListAdapter(
    private val users: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: RvItemUserListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = RvItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userId = users[position]
        holder.binding.userNameTextView.text = userId // You can change this to show name if available
        holder.itemView.setOnClickListener {
            onClick(userId)
        }
    }
}
