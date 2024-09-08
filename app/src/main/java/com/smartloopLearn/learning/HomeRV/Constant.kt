package com.smartloopLearn.learning.HomeRV

import com.smartloopLearn.learning.R

object Constant {

    fun getData() : ArrayList<RVModel> {
        val list = ArrayList<RVModel>()
        list.add(RVModel(R.drawable.web, "Web Development"))
        list.add(RVModel(R.drawable.python, "Python"))
        list.add(RVModel(R.drawable.ai, "Artificial Intelligence"))
        list.add(RVModel(R.drawable.digitalmarketing, "Digital Marketing"))
        list.add(RVModel(R.drawable.appdevelopment, "App Development"))
        list.add(RVModel(R.drawable.seo, "SEO"))

        return list
    }
}