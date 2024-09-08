package com.smartloopLearn.learning.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartloopLearn.QuizListAdapter
import com.smartloopLearn.learning.QuizModel
import com.smartloopLearn.learning.databinding.FragmentCourseBinding
import com.google.firebase.database.FirebaseDatabase

class CourseFragment : Fragment() {

    private var _binding: FragmentCourseBinding? = null
    private val binding get() = _binding!!

    private lateinit var quizModelList: MutableList<QuizModel>
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

        quizModelList = mutableListOf()
        setupRecyclerView()
        getDataFromFirebase()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun setupRecyclerView() {
        binding.progressBar.visibility = View.GONE
        adapter = QuizListAdapter(quizModelList)
        binding.recyler.layoutManager = LinearLayoutManager(requireContext())
        binding.recyler.adapter = adapter
    }

    private fun getDataFromFirebase() {
        binding.progressBar.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().reference
            .get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
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
                binding.progressBar.visibility = View.GONE
                // Handle the error appropriately
            }
    }

}
