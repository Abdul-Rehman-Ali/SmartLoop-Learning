package com.smartloopLearn.learning.admin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.smartloopLearn.learning.PdfDetailActivity
import com.smartloopLearn.learning.databinding.RowPdfUserBinding

class AdapterPdfUser :RecyclerView.Adapter<AdapterPdfUser.HolderPdfUser>, Filterable {

    private var context : Context
    public  var pdfArrayList : ArrayList<ModelPdf>
    private val filterList : ArrayList<ModelPdf>
    private lateinit var binding: RowPdfUserBinding

    private var filter : FilterPdfUser? = null

    constructor(context: Context, pdfArrayList: ArrayList<ModelPdf>) : super() {
        this.context = context
        this.pdfArrayList = pdfArrayList
        this.filterList = pdfArrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPdfUser.HolderPdfUser {
        binding = RowPdfUserBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderPdfUser(binding.root)

    }

    override fun getItemCount(): Int {
        return  pdfArrayList.size
    }

    override fun onBindViewHolder(holder: AdapterPdfUser.HolderPdfUser, position: Int) {
        val model = pdfArrayList[position]
        val pdfId = model.id
        val categoryId = model.categoryId
        val title = model.title
        val description = model.description
        val uid = model.uid
        val url = model.url
        val timestamp = model.timestamp

        val date = MyApplication.formatTimeStamp(timestamp)
        holder.tvtitle.text = title
        holder.tvdescription.text = description
        holder.tvdate.text = date

        MyApplication.loadCategory(categoryId, holder.tvcategory)
        MyApplication.loadPdfFromUrlSinglePage(url, title, holder.pdfviewer, holder.progressBar, null)
        MyApplication.loadPdfSize(url,title,holder.tvsize)


        holder.itemView.setOnClickListener {
            val intent = Intent(context, PdfDetailActivity::class.java)
            intent.putExtra("bookId", pdfId)
            context.startActivity(intent)
        }
    }

    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterPdfUser(filterList,this)
        }
        return filter as FilterPdfUser
    }


    inner class HolderPdfUser (itemView: View) : RecyclerView.ViewHolder(itemView){
        val pdfviewer = binding.pdfview
        val progressBar = binding.progressbar
        val tvtitle = binding.tvTitle
        val tvdescription = binding.tvDescription
        val tvcategory = binding.tvCategory
        val tvsize = binding.tvSize
        val tvdate = binding.tvDate
        val btnmore = binding.btnMore
    }
}