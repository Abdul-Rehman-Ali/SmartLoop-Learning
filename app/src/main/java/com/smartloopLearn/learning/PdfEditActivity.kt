package com.smartloopLearn.learning

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.smartloopLearn.learning.databinding.ActivityPdfEditBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PdfEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfEditBinding

    private companion object{
        private const val TAG = "PDF_EDIT_TAG"
    }

    private var bookId = ""
    private lateinit var progressDialog: ProgressDialog
    private lateinit var categoryTitleArrayList : ArrayList<String>
    private lateinit var categoryIdArrayList : ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize view binding
        binding = ActivityPdfEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookId = intent.getStringExtra("bookId")!!
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        loadCategory()
        loadBookInfo()

        binding.tvCategory.setOnClickListener {
            categoryDialog()
        }
        binding.btnSubmit.setOnClickListener {
            validateData()
        }
    }

    private fun loadBookInfo() {
        Log.d(TAG, "loadBookInfo: loading book info...")
        val ref = FirebaseDatabase.getInstance().getReference("Book")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    selectedCategoryId = snapshot.child("categoryId").value.toString()
                    val description = snapshot.child("description").value.toString()
                    val title = snapshot.child("title").value.toString()

                    binding.etTitle.setText(title)
                    binding.etDescription.setText(description)

                    Log.d(TAG, "onDataChange: loading book category info...")
                    val ref = FirebaseDatabase.getInstance().getReference("Category")
                    ref.child(selectedCategoryId)
                        .addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val category = snapshot.child("category").value
                                binding.tvCategory.text = category.toString()
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private var title = ""
    private var description = ""
    private fun validateData() {
        title = binding.etTitle.text.toString().trim()
        description = binding.etDescription.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, "Enter title", Toast.LENGTH_SHORT).show()
        } else if (description.isEmpty()){
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show()
        }else if (selectedCategoryId.isEmpty()){
            Toast.makeText(this, "Pick Category", Toast.LENGTH_SHORT).show()
        } else {
            updatePdf()
        }
    }

    private fun updatePdf() {
        Log.d(TAG, "updatePdf: Starting updating Pdf Info ...")
        progressDialog.setMessage("Updating Book Info")
        progressDialog.show()

        val hashMap= HashMap<String, Any>()
        hashMap["title"] = "$title"
        hashMap["description"] = "$description"
        hashMap["categoryId"] = "$selectedCategoryId"

        val ref = FirebaseDatabase.getInstance().getReference("Book")
        ref.child(bookId)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Log.d(TAG, "updatePdf: Updated Successfully...")
                Toast.makeText(this, "Updated Successfully" , Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e ->
                Log.d(TAG, "updatePdf: Failed to update due to ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to update due to ${e.message}" , Toast.LENGTH_SHORT).show()
            }
    }

    private var selectedCategoryId = ""
    private var selectedCategorytitle = ""
    private fun categoryDialog() {

        val categoriesArray = arrayOfNulls<String>(categoryTitleArrayList.size)
        for (i in categoryTitleArrayList.indices){
            categoriesArray[i] = categoryTitleArrayList[i]
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Category")
            .setItems(categoriesArray){ dialog, position ->
                selectedCategoryId = categoryIdArrayList[position]
                selectedCategorytitle = categoryTitleArrayList[position]

                binding.tvCategory.text = selectedCategorytitle
            }
            .show()
    }

    private fun loadCategory(){
        Log.d(TAG, "loadCategory: loading categories....")

        categoryTitleArrayList = ArrayList()
        categoryIdArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Category")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryTitleArrayList.clear()
                categoryIdArrayList.clear()

                for (ds in snapshot.children){
                    val id = "${ds.child("id").value}"
                    val category = "${ds.child("category").value}"

                    categoryIdArrayList.add(id)
                    categoryTitleArrayList.add(category)

                    Log.d(TAG, "onDataChange: Category Id $id")
                    Log.d(TAG, "onDataChange: Category $category")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
