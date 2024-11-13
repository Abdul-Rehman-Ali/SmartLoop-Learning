package com.smartloopLearn.learning.student.view.activities.coursedetails.frags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smartloopLearn.learning.R

class CourseReviewsFragment : Fragment(R.layout.fragment_course_reviews) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_reviews, container, false)
    }
}
