package com.smartloopLearn.learning.admin

import android.widget.Filter

class FilterCategory(
    private val filterList: ArrayList<ModelCategorey>,
    private val adapterCategory: AdapterCategory
) : Filter() {

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val results = FilterResults()

        if (constraint != null && constraint.isNotEmpty()) {
            val constraintString = constraint.toString().uppercase()
            val filteredModelList = ArrayList<ModelCategorey>()

            for (item in filterList) {
                if (item.category.uppercase().contains(constraintString)) {
                    filteredModelList.add(item)
                }
            }

            results.count = filteredModelList.size
            results.values = filteredModelList
        } else {
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapterCategory.categoryArrayList = results.values as ArrayList<ModelCategorey>
        adapterCategory.notifyDataSetChanged()
    }
}
