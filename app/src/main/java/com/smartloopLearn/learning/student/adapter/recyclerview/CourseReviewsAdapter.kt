package com.smartloopLearn.learning.student.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartloopLearn.learning.databinding.RvItemReviewCoursesBinding
import com.smartloopLearn.learning.student.model.CourseReview

class CourseReviewsAdapter(private val reviews: List<CourseReview>) :
    RecyclerView.Adapter<CourseReviewsAdapter.ReviewViewHolder>() {

    // ViewHolder to hold item_review layout elements using ViewBinding
    class ReviewViewHolder(private val binding: RvItemReviewCoursesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: CourseReview) {
            binding.textViewReviewerName.text = review.StudentName
            binding.ratingBarReview.rating = review.Rating.toFloat()
//            binding.textViewStudentLabel.text = review.role
            binding.textViewReviewDescription.text = review.Comment
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        // Inflate the view using ViewBinding
        val binding = RvItemReviewCoursesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        // Bind the data to the view
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int = reviews.size
}
