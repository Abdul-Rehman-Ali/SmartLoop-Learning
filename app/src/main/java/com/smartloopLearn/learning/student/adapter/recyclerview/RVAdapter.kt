package com.smartloopLearn.learning.student.adapter.recyclerview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.admin.DashboardUserActivity
import com.smartloopLearn.learning.student.model.RVModel

class RVAdapter(private val list: ArrayList<RVModel>, private val context: Context) : RecyclerView.Adapter<OurVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OurVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.course_sample_rv, parent, false)
        return OurVH(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: OurVH, position: Int) {
        val item = list[position]
        holder.img.setImageResource(item.image)
        holder.tv.text = item.tv

        // Handle click event for the first card view
        holder.itemView.setOnClickListener {
           // Check if it's the first item
                val intent = Intent(context, DashboardUserActivity::class.java)
                context.startActivity(intent)
        }
    }
}

class OurVH(view: View) : RecyclerView.ViewHolder(view) {
    val img: ImageView = view.findViewById(R.id.img)
    val tv: TextView = view.findViewById(R.id.tv_course)
}

//class RVAdapter(private val list: ArrayList<Courses>, private val context: Context) : RecyclerView.Adapter<OurVH>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OurVH {
//        val inflater = LayoutInflater.from(parent.context)
//        val view = inflater.inflate(R.layout.course_sample_rv, parent, false)
//        return OurVH(view)
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    override fun onBindViewHolder(holder: OurVH, position: Int) {
//        val item = list[position]
//        holder.img.setImageResource(item.courseImage)
//        holder.name.text = item.courseTitle
//        holder.tutor.text = item.tutorName
//        holder.rating.rating= item.rating.toFloat()
//
//        // Handle click event for the first card view
//        holder.itemView.setOnClickListener {
//            // Check if it's the first item
//            val intent = Intent(context, DashboardUserActivity::class.java)
//            context.startActivity(intent)
//        }
//    }
//}
//
//class OurVH(view: View) : RecyclerView.ViewHolder(view) {
//    val img: ImageView = view.findViewById(R.id.ivContinueCourseImage)
//    val name: TextView = view.findViewById(R.id.tvContinueCourseName)
//    val tutor: TextView = view.findViewById(R.id.tvContinueCourseTeacherName)
//    val rating : RatingBar = view.findViewById(R.id.rbContinueCourseRating)
//}
