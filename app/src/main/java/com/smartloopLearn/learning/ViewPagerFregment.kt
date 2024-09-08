package com.smartloopLearn.learning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smartloopLearn.learning.databinding.FragmentViewPagerFregmentBinding
import com.smartloopLearn.learning.onboarding_screens.FirstOnboardingScreen
import com.smartloopLearn.learning.onboarding_screens.SecondOnboardingFragment
import com.smartloopLearn.learning.onboarding_screens.ThirdOnboardingFragment

class ViewPagerFregment : Fragment() {

    private var _binding: FragmentViewPagerFregmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        _binding = FragmentViewPagerFregmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val fragmentList = arrayListOf<Fragment>(
            FirstOnboardingScreen(),
            SecondOnboardingFragment(),
            ThirdOnboardingFragment()
        )

        val adapter = ViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)

        // Access the ViewPager using the binding object
        binding.viewPager.adapter = adapter

        return view
    }

}