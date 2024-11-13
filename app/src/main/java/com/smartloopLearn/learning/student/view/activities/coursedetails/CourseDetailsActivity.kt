package com.smartloopLearn.learning.student.view.activities.coursedetails

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.tabs.TabLayout
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.databinding.ActivityCourseDetailsBinding
import com.smartloopLearn.learning.student.model.Courses
import com.smartloopLearn.learning.student.view.activities.coursedetails.frags.CourseLessonsFragment
import com.smartloopLearn.learning.student.view.activities.coursedetails.frags.CourseOverviewFragment
import com.smartloopLearn.learning.student.view.activities.coursedetails.frags.CourseReviewsFragment

class CourseDetailsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCourseDetailsBinding.inflate(layoutInflater) }
    private var courseData: Courses? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Set up for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get the course ID from the intent
        val courseId = intent.getStringExtra("CourseId") ?: return
        fetchCourseDetails(courseId)

        // Default fragment
        moveFrag(CourseOverviewFragment())

        // Set up tab listener to switch between fragments
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> moveFrag(CourseOverviewFragment())  // Overview
                    1 -> moveFrag(CourseLessonsFragment())  // Lessons
                    2 -> moveFrag(CourseReviewsFragment())  // Reviews
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun fetchCourseDetails(courseId: String) {
        val db = FirebaseFirestore.getInstance()
        val courseRef = db.collection("Courses").document(courseId)

        // Fetch course details from Firestore
        courseRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                // Extract course details from Firestore document
                val courseTitle = document.getString("CourseTitle") ?: ""
                val teacherName = document.getString("TeacherName") ?: ""
                val coursePrice = document.getString("CoursePrice") ?: ""
                val rating = document.getString("Rating") ?: ""
                val imageUrl = document.getString("ImageURL") ?: ""
                val courseDescription = document.getString("CourseDescription") ?: ""
                val courseDuration = document.getString("CourseDuration") ?: ""
                val discount = document.getString("Discount") ?: ""
                val certificate = document.getString("Certificate") ?: ""
                val courseLesson = document.getString("CourseLessons") ?: "" // Assuming one lesson

                // Create an instance of the Courses data class
                courseData = Courses(
                    CourseId = courseId,
                    CourseTitle = courseTitle,
                    TeacherName = teacherName,
                    ImageURL = imageUrl,
                    Rating = rating,
                    CoursePrice = coursePrice,
                    CourseDescription = courseDescription,
                    CourseDuration = courseDuration,
                    Discount = discount,
                    Certificate = certificate,
                    CourseLessons = courseLesson
                )

                // Move to CourseOverviewFragment
                moveFrag(CourseOverviewFragment())
            }
        }.addOnFailureListener {
            // Handle the error (optional)
        }
    }


    // Move to another fragment
    private fun moveFrag(frag: Fragment) {
        // If the fragment is an instance of CourseOverviewFragment, pass the course data
        if (frag is CourseOverviewFragment) {
            frag.arguments = Bundle().apply {
                putSerializable("course_data", courseData)
            }
        }
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flCourseDetails, frag)
            commit()
        }
    }
}
