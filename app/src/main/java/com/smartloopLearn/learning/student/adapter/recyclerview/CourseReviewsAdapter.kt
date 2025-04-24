package com.smartloopLearn.learning.student.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartloopLearn.learning.databinding.RvItemReviewCoursesBinding
import com.smartloopLearn.learning.student.model.CourseReview

class CourseReviewsAdapter(private var reviews: List<CourseReview>) :
    RecyclerView.Adapter<CourseReviewsAdapter.ReviewViewHolder>() {

    // ViewHolder to hold item_review layout elements using ViewBinding
    class ReviewViewHolder(private val binding: RvItemReviewCoursesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: CourseReview) {
            binding.textViewReviewerName.text = review.StudentName
            binding.ratingBarReview.rating = review.Rating.toFloat()
            binding.textViewReviewDescription.text = review.Comment
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = RvItemReviewCoursesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int = reviews.size

    // Method to update the list of reviews
    fun updateReviews(newReviews: List<CourseReview>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}
