package com.smartloopLearn.learning.chat.AdminChat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.smartloopLearn.learning.chat.Message
import com.smartloopLearn.learning.chat.MessageAdapter
import com.smartloopLearn.learning.databinding.ActivityAdminChatBinding

class AdminChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminChatBinding
    private lateinit var messageList: ArrayList<Message>
    private lateinit var adapter: MessageAdapter

    private val adminId = "Jj4vCt0OJSWYQcmavs1F7PSL6Vp2"
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get userId from intent
        userId = intent.getStringExtra("userId") ?: run {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialize message list and adapter
        messageList = ArrayList()
        adapter = MessageAdapter(messageList, adminId)

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = adapter

        val chatRef = FirebaseDatabase.getInstance().getReference("Chats")
        val adminRef = chatRef.child(adminId).child(userId).child("messages")
        val userRef = chatRef.child(userId).child(adminId).child("messages")

        // Listen for messages
        adminRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (msgSnap in snapshot.children) {
                    val msg = msgSnap.getValue(Message::class.java)
                    msg?.let { messageList.add(it) }
                }
                adapter.notifyDataSetChanged()
                binding.chatRecyclerView.scrollToPosition(messageList.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminChatActivity, "Error fetching messages", Toast.LENGTH_SHORT).show()
            }
        })

        // Send message
        binding.sendButton.setOnClickListener {
            val text = binding.messageEditText.text.toString().trim()
            if (text.isNotEmpty()) {
                val msg = Message(adminId, userId, text, System.currentTimeMillis())
                adminRef.push().setValue(msg)
                userRef.push().setValue(msg)
                binding.messageEditText.setText("")
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
