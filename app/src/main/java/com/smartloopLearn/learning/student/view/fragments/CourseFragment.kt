package com.smartloopLearn.learning.student.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartloopLearn.QuizListAdapter
import com.smartloopLearn.learning.QuizModel
import com.smartloopLearn.learning.databinding.FragmentCourseBinding
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.annotations.Async.Execute

class CourseFragment : Fragment() {

    private var _binding: FragmentCourseBinding? = null
    private val binding get() = _binding!!
    private var quizModelList: MutableList<QuizModel> = mutableListOf()
    private lateinit var adapter: QuizListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment using view binding
        _binding = FragmentCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromFirebase()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setupRecyclerView() {
        try {
            binding.customProgressBar.visibility = View.GONE
            adapter = QuizListAdapter(quizModelList)
            binding.recyler.adapter = adapter
            binding.recyler.layoutManager = LinearLayoutManager(requireContext())
        }catch (e : Exception) {
            Log.e("TestingFirebaseFetchExcetionHandler", "Exception in rv: ${e.message}")
            Log.e("TestingFirebaseFetchExcetionHandler", "Exception in rv: ${e.stackTrace}")
        }
    }

    private fun getDataFromFirebase() {

        binding.customProgressBar.visibility = View.VISIBLE
        try {
            FirebaseDatabase.getInstance().reference
                .get()
                .addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            Log.d("TestingFirebaseFetch", "SnapShot forEach ${snapshot}", )
                            // Check if the key is in the range of 0 to 4
                            val key = snapshot.key?.toIntOrNull()
                            if (key != null && key in 0..4) {
                                val quizModel = snapshot.getValue(QuizModel::class.java)
                                if (quizModel != null) {
                                    quizModelList.add(quizModel)
                                }
                            }
                        }
                    }
                    setupRecyclerView()
                }
                .addOnFailureListener {
                    binding.customProgressBar.visibility = View.GONE
                    showToast("Failed to fetch data")
                }
        }catch (e : Exception) {
            Log.e("TestingFirebaseFetchExceptionHandler", "Exception: ${e.message}")
            showToast("Failed to fetch data")
        }
            }

    private fun showToast(message: String) {
        binding.customProgressBar.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}
