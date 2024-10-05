package com.smartloopLearn.learning

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.smartloopLearn.learning.databinding.FragmentSplashBinding
import com.smartloopLearn.learning.student.view.activities.Home
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        // Delay using lifecycleScope to avoid memory leaks
        lifecycleScope.launch {
            delay(3000)
            if (finishedOnboarding()) {
                if (isUserLoggedIn()) {
                    // Navigate to the home activity if the user is logged in
                    val intent = Intent(requireActivity(), Home::class.java)
                    startActivity(intent)
                    requireActivity().finish() // Optional: Finish the splash activity
                } else {
                    // Navigate to the login or sign-up screen
                    findNavController().navigate(R.id.action_splashFragment_to_loginSignUp)
                }
            } else {
                // Navigate to the onboarding screen
                findNavController().navigate(R.id.action_splashFragment_to_viewPagerFregment)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Release the binding when the fragment view is destroyed
        _binding = null
    }

    private fun finishedOnboarding(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

    private fun isUserLoggedIn(): Boolean {
        val pref: SharedPreferences = requireActivity().getSharedPreferences("userLoginInfo", Context.MODE_PRIVATE)
        return pref.getBoolean("flag", false)
    }
}
