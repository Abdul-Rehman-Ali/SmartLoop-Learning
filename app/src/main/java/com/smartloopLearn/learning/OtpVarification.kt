package com.smartloopLearn.learning

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smartloopLearn.learning.databinding.ActivityOtpVarificationBinding

class OtpVarification : AppCompatActivity() {
    private lateinit var binding: ActivityOtpVarificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVarificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVerify.setOnClickListener {
            val i = Intent(this, CreateNewPassword::class.java)
            finish()
            startActivity(i)
        }

    }
}