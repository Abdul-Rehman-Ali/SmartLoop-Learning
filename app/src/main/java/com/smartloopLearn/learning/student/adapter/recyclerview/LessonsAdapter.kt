package com.smartloopLearn.learning.student.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.databinding.RvItemCourseLessonsBinding
import com.smartloopLearn.learning.student.model.CourseLessons

interface OnVideoClickListener {
    fun onVideoClick(videoURL: String)
}

class LessonsAdapter(
    private val lessons: List<CourseLessons>,
    private val videoClickListener: OnVideoClickListener // Pass listener in constructor
) : RecyclerView.Adapter<LessonsAdapter.LessonViewHolder>() {

    inner class LessonViewHolder(private val binding: RvItemCourseLessonsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lesson: CourseLessons) {
            binding.lessonTitle.text = lesson.title
            binding.lessonDescription.text = lesson.description
            binding.expandableContent.visibility = if (lesson.isExpanded) View.VISIBLE else View.GONE
            binding.arrowIcon.setImageResource(if (lesson.isExpanded) R.drawable.arrow_up else R.drawable.arrow_down)

            binding.parentLayout.setOnClickListener {
                lesson.isExpanded = !lesson.isExpanded
                notifyItemChanged(adapterPosition)
            }

            // Handle play video click
            binding.playVideo.setOnClickListener {
                videoClickListener.onVideoClick(lesson.videoURL)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val binding = RvItemCourseLessonsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LessonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(lessons[position])
    }

    override fun getItemCount() = lessons.size
}
