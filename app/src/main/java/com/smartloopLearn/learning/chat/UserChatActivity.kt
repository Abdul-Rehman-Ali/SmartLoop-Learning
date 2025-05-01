package com.smartloopLearn.learning.chat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.smartloopLearn.learning.databinding.ActivityUserChatBinding

class UserChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserChatBinding
    private lateinit var database: DatabaseReference
    private lateinit var messageList: ArrayList<Message>
    private lateinit var adapter: MessageAdapter

    private val senderId by lazy { FirebaseAuth.getInstance().uid ?: "" }
    private val receiverId = "Jj4vCt0OJSWYQcmavs1F7PSL6Vp2" // Admin ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messageList = ArrayList()
        adapter = MessageAdapter(messageList, senderId)

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = adapter

        val senderRef = FirebaseDatabase.getInstance().getReference("Chats").child(receiverId).child(senderId).child("messages")
        val receiverRef = FirebaseDatabase.getInstance().getReference("Chats").child(senderId).child(receiverId).child("messages")

        senderRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (msgSnap in snapshot.children) {
                    val msg = msgSnap.getValue(Message::class.java)
                    msg?.let { messageList.add(it) }
                }
                adapter.notifyDataSetChanged()
                binding.chatRecyclerView.scrollToPosition(messageList.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        binding.sendButton.setOnClickListener {
            val text = binding.messageEditText.text.toString().trim()
            if (text.isNotEmpty()) {
                val msg = Message(senderId, receiverId, text, System.currentTimeMillis())
                senderRef.push().setValue(msg)
                receiverRef.push().setValue(msg)
                binding.messageEditText.setText("")
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
