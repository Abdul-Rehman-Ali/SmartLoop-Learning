package com.smartloopLearn.learning.student.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.smartloopLearn.learning.student.Utils.Constant
import com.smartloopLearn.learning.student.Utils.Constant.getDataContinueCourses
import com.smartloopLearn.learning.student.Utils.Constant.getDataWeProvided
import com.smartloopLearn.learning.student.adapter.recyclerview.RVAdapter
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.databinding.FragmentHomeBinding
import com.smartloopLearn.learning.student.adapter.recyclerview.ContinueCoursesAdapter
import com.smartloopLearn.learning.student.adapter.recyclerview.WeProvided

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null // Declare binding variable
    private val binding get() = _binding!! // Non-null reference to binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

//        binding.webDevelopment.setOnClickListener {
//            try {
//                val intent = Intent(requireActivity(), WebDevelopment::class.java)
//                startActivity(intent)
//            } catch (e: Exception) {
//                // Handle exception (e.g., log error, display a message)
//            }
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load images and set up ImageSlider
        val imgList = ArrayList<SlideModel>()
        imgList.add(SlideModel((R.drawable.webdevelopmentslider), "Web Development"))
        imgList.add(SlideModel((R.drawable.pythonslider), "Python"))
        imgList.add(SlideModel((R.drawable.aislider), "Artificial Intelligence"))
        imgList.add(SlideModel((R.drawable.digitalmarketingslider), "Digital Marketing"))
        imgList.add(SlideModel((R.drawable.appdevelopmentslider), "App Development"))
        imgList.add(SlideModel((R.drawable.seoslider), "SEO"))

        binding.imageSlider.setImageList(imgList, ScaleTypes.FIT)

        // Recycler View
        binding.rv.adapter = RVAdapter(Constant.getData(), requireContext())
        binding.rv.layoutManager = GridLayoutManager(requireContext(), 2)


//        setUpContinueCoursesRV()

        setUpWeProvided()
    }

    private fun setUpWeProvided() {
        val adapter  = WeProvided(getDataWeProvided(), requireContext())

        binding.rvWeProvided.adapter = adapter
        binding.rvWeProvided.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setUpContinueCoursesRV() {


        val adapter  = ContinueCoursesAdapter(getDataContinueCourses(), requireContext())

        binding.rvContinueCourses.adapter = adapter
        binding.rvContinueCourses.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Nullify the binding reference to avoid memory leaks
        _binding = null
    }
}