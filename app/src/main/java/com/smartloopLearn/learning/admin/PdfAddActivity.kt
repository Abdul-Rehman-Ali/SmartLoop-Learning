package com.smartloopLearn.learning.admin

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.smartloopLearn.learning.databinding.ActivityPdfAddBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class PdfAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfAddBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var categoryArrayList: ArrayList<ModelCategorey>

    private var pdfUri: Uri? = null
    private var TAG = "PDF_ADD_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPdfAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        loadPdfCategories()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.tvCategory.setOnClickListener {
            categoryPickDialog()
        }

        binding.attach.setOnClickListener {
            pdfPickIntent()
        }

        binding.btnSubmit.setOnClickListener {
            validateData()
        }
    }

    private var title = ""
    private var description = ""
    private var category = ""
    private fun validateData() {
        Log.d(TAG, "validateData: validate data")
        title = binding.etTitle.text.toString().trim()
        description = binding.etDescription.text.toString().trim()
        category = binding.tvCategory.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show()
        } else if (description.isEmpty()) {
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show()
        } else if (category.isEmpty()) {
            Toast.makeText(this, "Pick Category", Toast.LENGTH_SHORT).show()
        } else if (pdfUri == null) {
            Toast.makeText(this, "Pick PDF", Toast.LENGTH_SHORT).show()
        } else {
            uploadPdfToStorage()
        }
    }

    private fun uploadPdfToStorage() {
        Log.d(TAG, "uploadPdfToStorage: uploading to storage...")
        progressDialog.setTitle("Uploading PDF...")
        progressDialog.show()

        val timestamp = System.currentTimeMillis()
        val filePathAndName = "Book/$timestamp"

        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageReference.putFile(pdfUri!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.d(TAG, "uploadPdfToStorage: PDF uploaded, now getting URL...")

                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadPdfUrl = "${uriTask.result}"

                uploadPdfIntoDb(uploadPdfUrl, timestamp)
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "uploadPdfToStorage: failed to upload due to ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadPdfIntoDb(uploadPdfUrl: String, timestamp: Long) {
        Log.d(TAG, "uploadPdfIntoDb: uploading to database...")
        progressDialog.setMessage("Uploading PDF into database...")

        val uid = firebaseAuth.uid
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["uid"] = "$uid"
        hashMap["id"] = "$timestamp"
        hashMap["title"] = "$title"
        hashMap["description"] = "$description"
        hashMap["categoryId"] = "$selectedCategoryId"
        hashMap["url"] = "$uploadPdfUrl"
        hashMap["timestamp"] = timestamp
        hashMap["viewCount"] = 0
        hashMap["downloadCount"] = 0

        val ref = FirebaseDatabase.getInstance().getReference("Book")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "uploadPdfIntoDb: Uploaded to db")
                progressDialog.dismiss()
                Toast.makeText(this, "Uploaded successfully...", Toast.LENGTH_SHORT).show()
                pdfUri = null
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "uploadPdfIntoDb: failed to upload due to ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadPdfCategories() {
        Log.d(TAG, "loadPdfCategories: Loading PDF Categories")
        categoryArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Category")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelCategorey::class.java)
                    categoryArrayList.add(model!!)
                    Log.d(TAG, "onDataChange: ${model.category}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "loadPdfCategories: onCancelled: ${error.message}")
            }
        })
    }

    private var selectedCategoryId = ""
    private var selectedCategoryTitle = ""
    private fun categoryPickDialog() {
        Log.d(TAG, "categoryPickDialog: Showing PDF Category Pick Dialog")
        val categoryArray = arrayOfNulls<String>(categoryArrayList.size)
        for (i in categoryArrayList.indices) {
            categoryArray[i] = categoryArrayList[i].category
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Category")
            .setItems(categoryArray) { dialog, which ->
                selectedCategoryTitle = categoryArrayList[which].category
                selectedCategoryId = categoryArrayList[which].id

                binding.tvCategory.text = selectedCategoryTitle
                Log.d(TAG, "categoryPickDialog: selected category id: $selectedCategoryId")
                Log.d(TAG, "categoryPickDialog: selected category title: $selectedCategoryTitle")
            }
            .show()
    }

    private fun pdfPickIntent(){
        Log.d(TAG, "pdfPickIntent : starting pdf pick intent")
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action=  Intent.ACTION_GET_CONTENT
        pdfAddActivityResultLauncher.launch(intent)
    }

    val pdfAddActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == RESULT_OK){
                Log.d(TAG, "PDF Picked ")
                pdfUri = result.data!!.data

            } else {
                Log.d(TAG, "PDF Picked Cancelled")
                Toast.makeText(this, "Cancelled",Toast.LENGTH_SHORT).show()
            }
        }
    )
}


