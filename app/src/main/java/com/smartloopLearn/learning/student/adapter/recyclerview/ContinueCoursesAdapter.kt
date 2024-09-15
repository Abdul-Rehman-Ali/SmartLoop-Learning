package com.smartloopLearn.learning.student.adapter.recyclerview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.admin.DashboardUserActivity
import com.smartloopLearn.learning.student.model.ContinueCourse


class ContinueCoursesAdapter(private val list: ArrayList<ContinueCourse>, private val context: Context) : RecyclerView.Adapter<ContinueCoursesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContinueCoursesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_item_home_frag_continue_courses, parent, false)
        return ContinueCoursesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ContinueCoursesViewHolder, position: Int) {
        val item = list[position]
        holder.img.setImageResource(item.courseImage)
        holder.name.text = item.courseTitle
        holder.tutor.text = item.tutorName
        holder.rating.rating= item.rating.toFloat()
        holder.progress.progress = item.progress

        // Handle click event for the first card view
        holder.itemView.setOnClickListener {
            // Check if it's the first item
            val intent = Intent(context, DashboardUserActivity::class.java)
            context.startActivity(intent)
        }
    }
}

class ContinueCoursesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val img: ImageView = view.findViewById(R.id.ivContinueCourseImage)
    val name: TextView = view.findViewById(R.id.tvContinueCourseName)
    val tutor: TextView = view.findViewById(R.id.tvContinueCourseTeacherName)
    val progress : ProgressBar = view.findViewById(R.id.pbContinueCourseProgress)
    val rating : RatingBar = view.findViewById(R.id.rbContinueCourseRating)
}
