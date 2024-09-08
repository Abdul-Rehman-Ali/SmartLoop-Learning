package com.smartloopLearn.learning

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smartloopLearn.learning.databinding.ActivityPasswordChangedBinding

class PasswordChanged : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordChangedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordChangedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackToLogin.setOnClickListener {
            val i = Intent(this, Login::class.java)
            finish()
            startActivity(i)
        }
    }
}