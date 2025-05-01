package com.smartloopLearn.learning.chat.AdminChat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.smartloopLearn.learning.databinding.ActivityUserListBinding
import com.smartloopLearn.learning.databinding.RvItemUserListBinding

class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding
    private lateinit var userList: ArrayList<String>
    private lateinit var adapter: UserListAdapter
    private val adminId = "Jj4vCt0OJSWYQcmavs1F7PSL6Vp2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userList = ArrayList()
        adapter = UserListAdapter(userList) { userId ->
            // ✅ THIS is where you send userId to AdminChatActivity
            val intent = Intent(this, AdminChatActivity::class.java)
            intent.putExtra("userId", userId)  // ✅ userId passed here
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        val chatRef = FirebaseDatabase.getInstance().getReference("Chats").child(adminId)
        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (userSnap in snapshot.children) {
                    val userId = userSnap.key ?: continue
                    userList.add(userId)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}

