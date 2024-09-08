package com.smartloopLearn.learning.admin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartloopLearn.learning.PdfDetailActivity
import com.smartloopLearn.learning.PdfEditActivity
import com.smartloopLearn.learning.databinding.RowPdfAdminBinding

class AdapterPdfAdmin:RecyclerView.Adapter<AdapterPdfAdmin.HolderPdfAdmin>, Filterable{


    private var context : Context
    public  var pdfArrayList : ArrayList<ModelPdf>
    private val filterList : ArrayList<ModelPdf>
    private lateinit var binding: RowPdfAdminBinding


    private var filter : FilterPdfAdmin? = null
    constructor(context: Context, pdfArrayList: ArrayList<ModelPdf>) : super() {
        this.context = context
        this.pdfArrayList = pdfArrayList
        this.filterList = pdfArrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPdfAdmin {
        binding = RowPdfAdminBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderPdfAdmin(binding.root)
    }

    override fun onBindViewHolder(holder: HolderPdfAdmin, position: Int) {
        val model = pdfArrayList[position]
        val pdfId = model.id
        val categoryId = model.categoryId
        val title = model.title
        val description = model.description
        val pdfUrl = model.url
        val timestamp = model.timestamp

        val formattedDate = MyApplication.formatTimeStamp(timestamp.toLong())
        holder.tvtitle.text = title
        holder.tvdescription.text = description
        holder.tvdate.text = formattedDate


        MyApplication.loadCategory(categoryId, holder.tvcategory)
        MyApplication.loadPdfFromUrlSinglePage(pdfUrl, title, holder.pdfviewer, holder.progressBar, null)
        MyApplication.loadPdfSize(pdfUrl,title,holder.tvsize)

        holder.btnmore.setOnClickListener {
            moreOptionDialog(model, holder.tvcategory)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, PdfDetailActivity::class.java)
            intent.putExtra("bookId", pdfId)
            context.startActivity(intent)
        }
    }

    private fun moreOptionDialog(model: ModelPdf, tvcategory: TextView) {
        val bookId = model.id
        val bookUrl = model.url
        val bookTitle = model.title

        val options = arrayOf("Edit", "Delete")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose Option")
            .setItems(options){ dialog, position ->
                    if (position == 0){
                        val intent = Intent(context, PdfEditActivity::class.java)
                        intent.putExtra("bookId", bookId)
                        context.startActivity(intent)
                    } else if (position == 1){
                        MyApplication.deleteBook(context, bookId, bookUrl, bookTitle)

                    }
            }
            .show()
    }

    override fun getItemCount(): Int {
        return  pdfArrayList.size
    }


    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterPdfAdmin(filterList, this)
        }
        return filter as FilterPdfAdmin
    }

    inner class HolderPdfAdmin (itemView: View) : RecyclerView.ViewHolder(itemView){
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