package com.smartloopLearn.learning

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smartloopLearn.learning.databinding.ActivityFeedbackBinding

class Feedback : AppCompatActivity() {
    private lateinit var binding: ActivityFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnSubmit.setOnClickListener {
            val message = binding.etFeedback.text.toString().trim()

            if (message.isNotEmpty()) {
                sendMessageOnWhatsApp(message)
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // feedback send to admin whatsapp
    private fun sendMessageOnWhatsApp(message: String) {
        val whatsappNumber = "+923181646340"
        val url = "https://api.whatsapp.com/send?phone=$whatsappNumber  + &text=${Uri.encode(message)}"

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)

    }
}