package com.smartloopLearn.learning.onboarding_screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.databinding.FragmentSecondOnboardingBinding

class SecondOnboardingFragment : Fragment() {


    private var _binding: FragmentSecondOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.next2.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)
            viewPager.currentItem = 2
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}