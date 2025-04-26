package com.smartloopLearn.learning.admin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.smartloopLearn.learning.databinding.ActivityShowAllReviewsBinding
import com.smartloopLearn.learning.student.adapter.recyclerview.CourseReviewsAdapter
import com.smartloopLearn.learning.student.model.CourseReview

//class ShowAllReviewsActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityShowAllReviewsBinding
//    private lateinit var adapter: AdapterShowAllReviews
//    private var reviewsList: List<CourseReview> = emptyList()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//
//        binding = ActivityShowAllReviewsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        // Initialize RecyclerView
//        adapter = AdapterShowAllReviews(reviewsList)
//        binding.recyclerViewReviews.layoutManager = LinearLayoutManager(this)
//        binding.recyclerViewReviews.adapter = adapter
//
//        // Load reviews from Firebase
//        loadReviews()
//    }
//
//    private fun loadReviews() {
//        val db = FirebaseFirestore.getInstance()
//
//        // Accessing the "Courses" collection
//        db.collection("Courses")
//            .get()
//            .addOnSuccessListener { courses ->
//                val fetchedReviews = mutableListOf<CourseReview>()
//
//                // Loop through each course document
//                for (course in courses) {
//                    // Accessing the "Reviews" subcollection of each course
//                    val reviewsRef = db.collection("Courses").document(course.id).collection("Reviews")
//
//                    reviewsRef.get()
//                        .addOnSuccessListener { reviews ->
//                            // Loop through each review document in the "Reviews" subcollection
//                            for (review in reviews) {
//                                val courseReview = review.toObject(CourseReview::class.java)
//                                fetchedReviews.add(courseReview)
//                            }
//
//                            // After fetching reviews from all courses, update the list and adapter
//                            reviewsList = fetchedReviews
//                            adapter.updateReviews(reviewsList)
//                        }
//                        .addOnFailureListener { e ->
//                            Toast.makeText(this, "Failed to load reviews: ${e.message}", Toast.LENGTH_SHORT).show()
//                        }
//                }
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(this, "Failed to load courses: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
//}


class ShowAllReviewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowAllReviewsBinding
    private lateinit var adapter: AdapterShowAllReviews
    private var reviewsList: List<ModelShowAllReviews> = emptyList() // Changed here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityShowAllReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize RecyclerView
        adapter = AdapterShowAllReviews(reviewsList) { review, isChecked ->
            updateReviewApprovalStatus(review, isChecked)
        }
        binding.recyclerViewReviews.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewReviews.adapter = adapter

        // Load reviews from Firebase
        loadReviews()
    }

    private fun loadReviews() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Courses")
            .get()
            .addOnSuccessListener { courses ->
                val fetchedReviews = mutableListOf<ModelShowAllReviews>() // Changed here
                var remainingCourses = courses.size()

                for (course in courses) {
                    val reviewsRef = db.collection("Courses")
                        .document(course.id)
                        .collection("Reviews")

                    reviewsRef.get()
                        .addOnSuccessListener { reviews ->
                            for (review in reviews) {
                                val modelReview = review.toObject(ModelShowAllReviews::class.java) // Changed here
                                modelReview.CourseId = course.id
                                modelReview.ReviewerId = review.id
                                fetchedReviews.add(modelReview)
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to load reviews: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                        .addOnCompleteListener {
                            remainingCourses--
                            if (remainingCourses == 0) {
                                reviewsList = fetchedReviews
                                adapter.updateReviews(reviewsList)
                            }
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load courses: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateReviewApprovalStatus(review: ModelShowAllReviews, isChecked: Boolean) {
        val db = FirebaseFirestore.getInstance()

        val reviewRef = db.collection("Courses")
            .document(review.CourseId ?: "")
            .collection("Reviews")
            .document(review.ReviewerId ?: "")

        reviewRef.update("Approved", isChecked)
            .addOnSuccessListener {
//                Toast.makeText(this, "Review approval status updated", Toast.LENGTH_SHORT).show()

                //  Update local list to immediately reflect user change
                val index = reviewsList.indexOfFirst { it.CourseId == review.CourseId && it.ReviewerId == review.ReviewerId }
                if (index != -1) {
                    val updatedReview = reviewsList[index].copy(Approved = isChecked)
                    reviewsList = reviewsList.toMutableList().apply {
                        set(index, updatedReview)
                    }
                    adapter.updateReviews(reviewsList)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update approval status: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
