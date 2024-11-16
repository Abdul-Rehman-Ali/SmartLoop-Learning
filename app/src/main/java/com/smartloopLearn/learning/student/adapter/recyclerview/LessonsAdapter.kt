package com.smartloopLearn.learning.student.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.databinding.RvItemCourseLessonsBinding
import com.smartloopLearn.learning.student.model.CourseLessons

class LessonsAdapter(private val lessons: List<CourseLessons>) :
    RecyclerView.Adapter<LessonsAdapter.LessonViewHolder>() {

    // ViewHolder using View Binding
    inner class LessonViewHolder(private val binding: RvItemCourseLessonsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lesson: CourseLessons) {
            // Bind the lesson title
            binding.lessonTitle.text = lesson.title

            // Bind the lesson description
            binding.lessonDescription.text = lesson.description

            // Handle expandable content visibility
            val isExpanded = lesson.isExpanded
            binding.expandableContent.visibility = if (isExpanded) View.VISIBLE else View.GONE
            binding.arrowIcon.setImageResource(if (isExpanded) R.drawable.arrow_up else R.drawable.arrow_down)

            // Toggle expansion on click
            binding.parentLayout.setOnClickListener {
                lesson.isExpanded = !lesson.isExpanded
                notifyItemChanged(adapterPosition)
            }

            // Store videoURL in playVideo ImageView using setTag
            binding.playVideo.tag = lesson.videoURL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        // Inflate the layout using View Binding
        val binding = RvItemCourseLessonsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LessonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(lessons[position])
    }

    override fun getItemCount() = lessons.size
}
