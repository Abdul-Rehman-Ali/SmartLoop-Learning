package com.smartloopLearn.learning.student.view.activities.coursedetails.frags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.databinding.FragmentCourseOverviewBinding
import com.smartloopLearn.learning.student.adapter.recyclerview.SkillsAdapter
import com.smartloopLearn.learning.student.model.Courses

class CourseOverviewFragment : Fragment() {

    private var _binding: FragmentCourseOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var courseData: Courses
    private lateinit var skillsAdapter: SkillsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show progress bar before loading content
        binding.customProgressBar.visibility = View.VISIBLE

        // Retrieve data passed from CourseDetailsActivity
        courseData = arguments?.getSerializable("course_data") as? Courses ?: return

        // Populate the UI with the course data
        binding.tvCourseTitle.text = courseData.CourseTitle
        binding.tvTeacherName.text = courseData.TeacherName
        binding.CourseRating.rating = courseData.Rating.toFloatOrNull() ?: 0f
        binding.tvCoursePrice.text = "${courseData.CoursePrice}"
        binding.tvCourseDescription.text = courseData.CourseDescription
        binding.tvCourseDuration.text = courseData.CourseDuration
        binding.tvDiscount.text = courseData.Discount
        binding.tvCertificate.text = courseData.Certificate
        binding.tvCourseLessons.text = courseData.CourseLessons

        // Initialize the adapter with the skills list
        val skillsList = courseData.Skills ?: emptyList()
        skillsAdapter = SkillsAdapter(skillsList)

        // Set up RecyclerView for skills
        val skillsRecyclerView = view.findViewById<RecyclerView>(R.id.skillsRecyclerView)
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        skillsRecyclerView.layoutManager = gridLayoutManager
        skillsRecyclerView.adapter = skillsAdapter

        // Hide progress bar once data is displayed
        binding.customProgressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}