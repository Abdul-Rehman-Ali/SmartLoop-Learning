package com.smartloopLearn.learning.onboarding_screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.databinding.FragmentThirdOnboardingBinding

class ThirdOnboardingFragment : Fragment() {

    private var _binding: FragmentThirdOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.letsGo.setOnClickListener {
                finishedOnboarding()
                findNavController().navigate(R.id.action_viewPagerFregment_to_loginSignUp)
        }
    }

    private fun finishedOnboarding() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("Finished", true)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
