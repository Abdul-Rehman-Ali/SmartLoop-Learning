package com.smartloopLearn.learning.student.view.activities.coursedetails.frags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartloopLearn.learning.databinding.FragmentCourseLessonsBinding
import com.smartloopLearn.learning.student.adapter.recyclerview.LessonsAdapter
import com.smartloopLearn.learning.student.model.CourseLessons
import com.smartloopLearn.learning.student.view.activities.coursedetails.CourseDetailsActivity

class CourseLessonsFragment : Fragment() {
    private lateinit var binding: FragmentCourseLessonsBinding
    private var lessons: List<CourseLessons> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCourseLessonsBinding.inflate(inflater, container, false)

        // Retrieve lessons data passed from the activity
        arguments?.let {
            lessons = it.getParcelableArrayList("lessons") ?: mutableListOf()
        }

        // Ensure that the activity implements the OnVideoClickListener interface
        val listener = activity as? CourseDetailsActivity
            ?: throw ClassCastException("${activity?.javaClass?.simpleName} must implement OnVideoClickListener")

        // Set up RecyclerView
        binding.recyclerViewLessons.layoutManager = LinearLayoutManager(context)
        val lessonsAdapter = LessonsAdapter(lessons, listener)  // Pass listener here
        binding.recyclerViewLessons.adapter = lessonsAdapter

        return binding.root
    }
}
