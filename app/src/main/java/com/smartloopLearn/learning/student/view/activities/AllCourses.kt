package com.smartloopLearn.learning.student.view.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.smartloopLearn.learning.databinding.ActivityAllCoursesBinding
import com.smartloopLearn.learning.student.adapter.recyclerview.AllCoursesAdapter
import com.smartloopLearn.learning.student.model.Courses

class AllCourses : AppCompatActivity() {

    private val coursesList = ArrayList<Courses>() // Use Course model
    private lateinit var AllcoursesAdapter: AllCoursesAdapter
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
        AllcoursesAdapter = AllCoursesAdapter(coursesList, this) // Initialize your adapter
        binding.rvAllCourses.adapter = AllcoursesAdapter
        binding.rvAllCourses.layoutManager = LinearLayoutManager(this)
        fetchCoursesFromFirestore()
    }

    private fun fetchCoursesFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Courses")
            .get()
            .addOnSuccessListener { result ->
                coursesList.clear()
                for (document in result) {
                    val course = document.toObject(Courses::class.java) // Change to Course model
                    coursesList.add(course)
                    Log.d("Firestore Data", "Fetched Course: $course") // Log each course
                }
                AllcoursesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore Error", "Error getting documents: ", exception)
            }
    }
}
