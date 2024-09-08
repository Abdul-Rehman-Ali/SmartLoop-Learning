package com.smartloopLearn.learning.admin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog // Import the correct AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.smartloopLearn.learning.databinding.RowCategoreyBinding
import com.google.firebase.database.FirebaseDatabase

class AdapterCategory(private val context: Context, var categoryArrayList: ArrayList<ModelCategorey>) : RecyclerView.Adapter<AdapterCategory.HolderCategory>(), Filterable {

    private lateinit var binding: RowCategoreyBinding
    private var filterable: ArrayList<ModelCategorey> = ArrayList(categoryArrayList)
    private var filter: FilterCategory? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        binding = RowCategoreyBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderCategory(binding.root)
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
        // get data
        val model = categoryArrayList[position]
        val id = model.id
        val category = model.category
        val uid = model.uid
        val timestamp = model.timestamp

        // set data
        holder.categoryTv.text = category

        holder.deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
                .setMessage("Are you sure to delete this category?")
                .setPositiveButton("Confirm") { dialog, which ->
                    Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show()
                    deleteCategory(model, holder)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, PdfListAdminActivity::class.java )
            intent.putExtra("categoryId" , id)
            intent.putExtra("category" , category)
            context.startActivity(intent)
        }
    }

    private fun deleteCategory(model: ModelCategorey, holder: HolderCategory) {
        val id = model.id
        val ref = FirebaseDatabase.getInstance().getReference("Category")
        ref.child(id) // Make sure to use the correct id
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Unable to delete due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    inner class HolderCategory(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryTv: TextView = binding.tvCategory
        var deleteBtn: ImageButton = binding.btnDelete
    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilterCategory(filterable, this)
        }
        return filter as FilterCategory
    }

    class FilterCategory(
        private val filterList: ArrayList<ModelCategorey>,
        private val adapter: AdapterCategory
    ) : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<ModelCategorey>()

            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(filterList)
            } else {
                val filterPattern = constraint.toString().lowercase().trim()

                for (item in filterList) {
                    if (item.category.lowercase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            adapter.categoryArrayList.clear()
            if (results != null && results.values != null) {
                adapter.categoryArrayList.addAll(results.values as ArrayList<ModelCategorey>)
            }
            adapter.notifyDataSetChanged()
        }
    }
}
