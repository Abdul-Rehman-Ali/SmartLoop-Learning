package com.smartloopLearn.learning

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smartloopLearn.learning.auth.Login
import com.smartloopLearn.learning.databinding.ActivityMainBinding
import com.smartloopLearn.learning.student.view.activities.Home

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val sharedPreferences = getSharedPreferences("userLoginInfo", Context.MODE_PRIVATE)
//        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
//
//        if (isLoggedIn) {
//            // User is logged in, navigate to HomeActivity
//            val intent = Intent(this, Home::class.java)
//            startActivity(intent)
//            finish() // Close the splash screen
//        } else {
//            // User is not logged in, navigate to LoginActivity
//            val intent = Intent(this, Login::class.java)
//            startActivity(intent)
//            finish() // Close the splash screen
//        }
    }
}
