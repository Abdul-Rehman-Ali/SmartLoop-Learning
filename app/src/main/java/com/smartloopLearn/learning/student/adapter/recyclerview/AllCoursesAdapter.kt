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
import com.smartloopLearn.learning.student.model.AllCourses
import com.smartloopLearn.learning.student.model.Courses


class AllCoursesAdapter(private val list: ArrayList<AllCourses>, private val context: Context) : RecyclerView.Adapter<AllCoursesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCoursesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_item_all_courses, parent, false)
        return AllCoursesViewHolder(view)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AllCoursesViewHolder, position: Int) {
        val item = list[position]
        holder.img.setImageResource(item.courseImage)
        holder.name.text = item.courseTitle
        holder.tutor.text = item.tutorName
        holder.rating.rating= item.rating.toFloat()
        holder.price.text = item.price.toString()

        // Handle click event for the first card view
        holder.itemView.setOnClickListener {
            // Check if it's the first item
            val intent = Intent(context, DashboardUserActivity::class.java)
            context.startActivity(intent)
        }
    }
}

class AllCoursesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val img: ImageView = view.findViewById(R.id.ivAllCourseImage)
    val name: TextView = view.findViewById(R.id.tvAllCourseName)
    val tutor: TextView = view.findViewById(R.id.tvAllCourseTeacherName)
    val rating : RatingBar = view.findViewById(R.id.rbAllCourseRating)
    val price : TextView = view.findViewById(R.id.tvAllCoursePrice)
}