package com.smartloopLearn.learning.student.view.activities.coursedetails.frags

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.databinding.FragmentCourseReviewsBinding
import com.smartloopLearn.learning.student.adapter.recyclerview.CourseReviewsAdapter
import com.smartloopLearn.learning.student.model.CourseReview

class CourseReviewsFragment : Fragment(R.layout.fragment_course_reviews) {
    private lateinit var binding: FragmentCourseReviewsBinding
    private lateinit var adapter: CourseReviewsAdapter
    private var reviews: List<CourseReview> = emptyList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCourseReviewsBinding.bind(view)

        // Retrieve reviews from arguments passed from the activity
        arguments?.let {
            val allReviews = it.getParcelableArrayList<CourseReview>("reviews") ?: emptyList()

            // Filter only the reviews where approved == true
            reviews = allReviews.filter { review ->
                review.Approved == true
            }
        }

        // Set up the RecyclerView
        adapter = CourseReviewsAdapter(reviews)
        binding.recyclerViewReviews.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewReviews.adapter = adapter
    }
}


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding = FragmentCourseReviewsBinding.bind(view)
//
//        // Retrieve reviews from arguments passed from the activity
//        arguments?.let {
//            reviews = it.getParcelableArrayList("reviews") ?: emptyList()
//        }
//
//        // Set up the RecyclerView
//        adapter = CourseReviewsAdapter(reviews)
//        binding.recyclerViewReviews.layoutManager = LinearLayoutManager(requireContext())
//        binding.recyclerViewReviews.adapter = adapter
//    }