package com.smartloopLearn.learning

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smartloopLearn.learning.databinding.ActivityLoginSignUpBinding

class LoginSignUp : AppCompatActivity() {
    private lateinit var binding: ActivityLoginSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val i = Intent(this, Login::class.java)
            startActivity(i)
        }

        binding.btnSignUp.setOnClickListener {
            val i = Intent(this, SignUp::class.java)
            startActivity(i)
        }

        // Facebook open
        binding.facebook.setOnClickListener {
            val facebookIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/abdul-rehman-ali-124523223"))

            // Check if Facebook app is installed
            if (facebookIntent.resolveActivity(packageManager) != null) {
                startActivity(facebookIntent)
            } else {
                // Open Facebook website in browser
                val websiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/abdul-rehman-ali-124523223"))
                startActivity(websiteIntent)
            }
        }

        // instagam open
        binding.instagram.setOnClickListener {
            val facebookIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ch.abdulrehmanali/"))

            // if Facebook installed
            if (facebookIntent.resolveActivity(packageManager) != null) {
                startActivity(facebookIntent)
            } else {
                // Open Facebook website in browser
                val websiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ch.abdulrehmanali/"))
                startActivity(websiteIntent)
            }
        }

    }
}