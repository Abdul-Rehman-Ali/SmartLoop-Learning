package com.smartloopLearn.learning.student.adapter.recyclerview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.admin.DashboardUserActivity
import com.smartloopLearn.learning.student.model.Courses


class Courses(private val list: ArrayList<Courses>, private val context: Context) : RecyclerView.Adapter<CoursesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_item_frag_home_courses, parent, false)
        return CoursesViewHolder(view)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CoursesViewHolder, position: Int) {
        val item = list[position]
        holder.img.setImageResource(item.courseImage)
        holder.name.text = item.courseTitle
        holder.tutor.text = item.tutorName
        holder.rating.rating= item.rating.toFloat()

        // Handle click event for the first card view
        holder.itemView.setOnClickListener {
            // Check if it's the first item
            val intent = Intent(context, DashboardUserActivity::class.java)
            context.startActivity(intent)
        }
    }
}

class CoursesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val img: ImageView = view.findViewById(R.id.ivCourseImage)
    val name: TextView = view.findViewById(R.id.tvCourseName)
    val tutor: TextView = view.findViewById(R.id.tvCourseTeacherName)
    val rating : RatingBar = view.findViewById(R.id.rbCourseRating)
}
