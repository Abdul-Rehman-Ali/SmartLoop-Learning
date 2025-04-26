package com.smartloopLearn.learning.admin


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartloopLearn.learning.databinding.RvItemReviewsNotifyBinding
import com.smartloopLearn.learning.student.model.CourseReview

//class AdapterShowAllReviews(private var reviews: List<CourseReview>) :
//    RecyclerView.Adapter<AdapterShowAllReviews.ReviewViewHolder>() {
//
//    // ViewHolder to hold item_review layout elements using ViewBinding
//    class ReviewViewHolder(private val binding: RvItemReviewsNotifyBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(review: CourseReview) {
//            binding.textViewReviewerName.text = review.StudentName
//            binding.ratingBarReview.rating = review.Rating.toFloat()
//            binding.textViewReviewDescription.text = review.Comment
//            binding.switchApproval.isChecked = review.Approved
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
//        val binding = RvItemReviewsNotifyBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//        return ReviewViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
//        holder.bind(reviews[position])
//    }
//
//    override fun getItemCount(): Int = reviews.size
//
//    // Method to update the list of reviews
//    fun updateReviews(newReviews: List<CourseReview>) {
//        reviews = newReviews
//        notifyDataSetChanged()
//    }
//}


class AdapterShowAllReviews(
    private var reviewsList: List<ModelShowAllReviews>,
    private val onApprovalChanged: (ModelShowAllReviews, Boolean) -> Unit
) : RecyclerView.Adapter<AdapterShowAllReviews.ViewHolder>() {

    inner class ViewHolder(val binding: RvItemReviewsNotifyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ModelShowAllReviews) {
            binding.textViewReviewerName.text = review.StudentName

            // 👇 Very important: set the Switch according to Approved field
            binding.switchApproval.isChecked = review.Approved == true

            // 👇 Set listener after setting isChecked, and remove old listener first
            binding.switchApproval.setOnCheckedChangeListener(null)
            binding.switchApproval.setOnCheckedChangeListener { _, isChecked ->
                onApprovalChanged(review, isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemReviewsNotifyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reviewsList[position])
    }

    override fun getItemCount(): Int = reviewsList.size

    fun updateReviews(newReviews: List<ModelShowAllReviews>) {
        reviewsList = newReviews
        notifyDataSetChanged()
    }
}
