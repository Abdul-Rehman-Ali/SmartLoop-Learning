package com.smartloopLearn.learning.student.view.fragments

import CoursesAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.FirebaseFirestore
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.databinding.FragmentHomeBinding
import com.smartloopLearn.learning.student.Utils.Constant.getDataContinueCourses
import com.smartloopLearn.learning.student.Utils.Constant.getDataWeProvided
import com.smartloopLearn.learning.student.adapter.recyclerview.ContinueCoursesAdapter
import com.smartloopLearn.learning.student.adapter.recyclerview.WeProvided
import com.smartloopLearn.learning.student.model.Courses
import com.smartloopLearn.learning.student.view.activities.AllCourses

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Firestore-related variables
    private val coursesList = ArrayList<Courses>()
    private lateinit var coursesAdapter: CoursesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // "All Courses" button click
        binding.allCourses.setOnClickListener {
            val intent = Intent(requireActivity(), AllCourses::class.java)
            startActivity(intent)
        }

        // Set up the image slider
        val imgList = ArrayList<SlideModel>()
        imgList.add(SlideModel((R.drawable.webdevelopmentslider), "Web Development"))
        imgList.add(SlideModel((R.drawable.pythonslider), "Python"))
        imgList.add(SlideModel((R.drawable.aislider), "Artificial Intelligence"))
        imgList.add(SlideModel((R.drawable.digitalmarketingslider), "Digital Marketing"))
        imgList.add(SlideModel((R.drawable.appdevelopmentslider), "App Development"))
        imgList.add(SlideModel((R.drawable.seoslider), "SEO"))

        binding.imageSlider.setImageList(imgList, ScaleTypes.FIT)

        // Initialize RecyclerViews
        setUpCoursesRV() // Courses RecyclerView
        setUpWeProvided() // Horizontal RecyclerView for other data
        setUpContinueCoursesRV() // Continue Courses RecyclerView

        // Fetch courses from Firestore
//        fetchCoursesFromFirestore()
    }

    private fun setUpCoursesRV() {
        coursesAdapter = CoursesAdapter(coursesList, requireContext())
        binding.rv.adapter = coursesAdapter
        binding.rv.layoutManager = GridLayoutManager(requireContext(), 2)
        fetchCoursesFromFirestore()
    }

    private fun setUpWeProvided() {
        val adapter = WeProvided(getDataWeProvided(), requireContext())
        binding.rvWeProvided.adapter = adapter
        binding.rvWeProvided.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setUpContinueCoursesRV() {
        val adapter = ContinueCoursesAdapter(getDataContinueCourses(), requireContext())
        binding.rvContinueCourses.adapter = adapter
        binding.rvContinueCourses.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun fetchCoursesFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Courses")
            .get()
            .addOnSuccessListener { result ->
                coursesList.clear()
                for (document in result) {
                    val course = document.toObject(Courses::class.java)
                    coursesList.add(course)
                    Log.d("Firestore Data", "Fetched Course: $course")  // Log each course
                }
                coursesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore Error", "Error getting documents: ", exception)
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Nullify the binding reference to avoid memory leaks
    }
}
