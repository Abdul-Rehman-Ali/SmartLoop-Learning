package com.smartloopLearn.learning.admin
import android.widget.Filter


class FilterPdfUser: Filter {

    var filterList: ArrayList<ModelPdf>

    var adapterPdfUser:AdapterPdfUser

    constructor(filterList: ArrayList<ModelPdf>, adapterPdfUser: AdapterPdfUser) {
        this.filterList = filterList
        this.adapterPdfUser = adapterPdfUser
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint: CharSequence? = constraint
        val results = FilterResults()

        if (constraint != null && constraint.isNotEmpty()) {
            constraint = constraint.toString().uppercase()
            val filterModel = ArrayList<ModelPdf>()
            for (i in filterList.indices) {
                if (filterList[i].title.uppercase().contains(constraint)) {
                    filterModel.add(filterList[i])
                }
            }
            results.count = filterModel.size
            results.values = filterModel
        }
        else {
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence, results: FilterResults) {
        adapterPdfUser.pdfArrayList = results.values as ArrayList<ModelPdf>

        adapterPdfUser.notifyDataSetChanged()
    }

}