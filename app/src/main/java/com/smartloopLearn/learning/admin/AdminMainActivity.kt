package com.smartloopLearn.learning.admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.smartloopLearn.learning.chat.AdminChat.UserListActivity
import com.smartloopLearn.learning.databinding.ActivityAdminMainBinding

class AdminMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Open DashboardActivity when "Add Job" card is clicked
        binding.cardAddJob.setOnClickListener {
            startActivity(Intent(this, DashboardAdmin::class.java))
        }

        binding.cardCheckReviews.setOnClickListener {
            startActivity(Intent(this, ShowAllReviewsActivity::class.java))
        }

        binding.tvRealAdminchat.setOnClickListener {
            startActivity(Intent(this, UserListActivity::class.java))
        }
    }
}
