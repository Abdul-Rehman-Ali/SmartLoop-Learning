package com.smartloopLearn.learning.student.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.student.model.WeProvided


class WeProvided(private val list: ArrayList<WeProvided>, private val context: Context) : RecyclerView.Adapter<WeProvidedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeProvidedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_item_home_frag_we_provided, parent, false)
        return WeProvidedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: WeProvidedViewHolder, position: Int) {
        val item = list[position]
        holder.img.setImageResource(item.image)
        holder.name.text = item.name

        // Handle click event for the first card view
//        holder.itemView.setOnClickListener {
//            val intent = Intent(context, DashboardUserActivity::class.java)
//            context.startActivity(intent)
//        }
    }
}

class WeProvidedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val img: ImageView = view.findViewById(R.id.ivWeProvided)
    val name: TextView = view.findViewById(R.id.tvWeProvided)
}
