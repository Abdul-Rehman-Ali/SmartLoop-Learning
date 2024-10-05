package com.smartloopLearn.learning.student.view.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartloopLearn.learning.databinding.ActivityAllCoursesBinding
import com.smartloopLearn.learning.student.Utils.Constant.getDataAllCourses
import com.smartloopLearn.learning.student.adapter.recyclerview.AllCoursesAdapter

class AllCourses : AppCompatActivity() {

    private lateinit var binding: ActivityAllCoursesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize view binding
        binding = ActivityAllCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the RecyclerView
        setUpAllCoursesRV()

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setUpAllCoursesRV() {

        val adapter = AllCoursesAdapter(getDataAllCourses(), this)

        binding.rvAllCourses.adapter = adapter
        binding.rvAllCourses.layoutManager = LinearLayoutManager(this)
    }
}
