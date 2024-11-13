package com.smartloopLearn.learning.student.adapter.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartloopLearn.learning.R

class SkillsAdapter(private val skills: List<String>) : RecyclerView.Adapter<SkillsAdapter.SkillViewHolder>() {

    class SkillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val skillNameTextView: TextView = itemView.findViewById(R.id.skillName)

        fun bind(skill: String) {
            skillNameTextView.text = skill
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_skills, parent, false)
        return SkillViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        holder.bind(skills[position])
    }

    override fun getItemCount(): Int = skills.size
}
